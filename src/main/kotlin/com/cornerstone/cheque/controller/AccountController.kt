package com.cornerstone.cheque.controller

import com.cornerstone.cheque.model.Account
import com.cornerstone.cheque.model.AccountRequest
import com.cornerstone.cheque.model.AccountResponse
import com.cornerstone.cheque.repo.AccountRepository
import com.cornerstone.cheque.service.AccountService
import com.cornerstone.cheque.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/accounts")
class AccountController(private val accountService: AccountService,
                        private val userService: UserService,
                        private val accountrepo: AccountRepository) {

    @PostMapping("/create")
    fun createAccount(
        @RequestBody request: AccountRequest,
        principal: Principal
    ): ResponseEntity<out Any?> {
        val user = userService.findByUsername(principal.name)
        val userId = user?.id ?: throw IllegalStateException("Unexpected user has no id...")
        return ResponseEntity.ok(accountService.create(userId, request))
    }
    @GetMapping("/getAll")
    fun getAll(): ResponseEntity<List<AccountResponse>> =
        ResponseEntity.ok(accountService.getAll())

    @GetMapping("/{accountNumber}")
    fun getByAccountNumber(@PathVariable accountNumber: String): ResponseEntity<AccountResponse> {
        val result = accountService.getByAccountNumber(accountNumber)
        return if (result != null) ResponseEntity.ok(result)
        else ResponseEntity.notFound().build()
    }

    @GetMapping("/my")
    fun getMyAccounts(principal: Principal): ResponseEntity<List<AccountResponse>> {
        val userId = principal.name.toLong()
        val accounts = accountService.getByUserId(userId)
        return ResponseEntity.ok(accounts)
    }

}
