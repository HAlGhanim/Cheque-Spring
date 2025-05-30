package com.cornerstone.cheque.controller

import com.cornerstone.cheque.model.AccountResponse
import com.cornerstone.cheque.model.DepositRequest
import com.cornerstone.cheque.service.AccountService
import com.cornerstone.cheque.service.TransactionService
import com.cornerstone.cheque.service.UserService
import com.cornerstone.cheque.repo.AccountRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    private val service: TransactionService,
    private val accountRepository: AccountRepository,
    private val userService: UserService,
    private val accountService: AccountService
) {

    @PreAuthorize("hasRole('USER','ADMIN')")
    @PostMapping("/deposit")
    fun deposit(@RequestBody request: DepositRequest, principal: Principal): ResponseEntity<AccountResponse> {
        val user = userService.findByEmail(principal.name)
            ?: throw IllegalStateException("User not authenticated")

        val account = accountService.getByUserId(user.email).firstOrNull()
            ?: throw IllegalArgumentException("User does not have an account")

        val updated = service.deposit(request.amount, accountRepository.findByAccountNumber(account.accountNumber)!!)
        return ResponseEntity.ok(accountService.getByAccountNumber(updated.accountNumber))
    }

    @PreAuthorize("hasRole('USER','ADMIN')")
    @PostMapping("/withdraw")
    fun withdraw(@RequestBody request: DepositRequest, principal: Principal): ResponseEntity<AccountResponse> {
        val user = userService.findByEmail(principal.name)
            ?: throw IllegalStateException("User not authenticated")

        val account = accountService.getByUserId(user.email).firstOrNull()
            ?: throw IllegalArgumentException("User does not have an account")

        val updated = service.withdraw(request.amount, accountRepository.findByAccountNumber(account.accountNumber)!!)
        return ResponseEntity.ok(accountService.getByAccountNumber(updated.accountNumber))
    }
}