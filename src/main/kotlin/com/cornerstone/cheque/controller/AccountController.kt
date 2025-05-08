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
class AccountController(private val service: AccountService,
                        private val userService: UserService,
                        private val accountrepo: AccountRepository) {

    @PostMapping("/create")
    fun createAccount(
        @RequestBody request: AccountRequest,
        principal: Principal
    ): ResponseEntity<out Any?> {
        val user = userService.findByUsername(principal.name)

        if (accountrepo.existsByUser(user)) {
            return ResponseEntity
                .badRequest()
                .body(mapOf("error" to "User already has an account"))
        }
        val account = Account(
            accountNumber = service.generateUniqueAccountNumber(accountrepo),
            user = user,
            balance = request.balance,
            spendingLimit = request.spendingLimit,
            currency = request.currency,
            accountType = request.accountType,
            createdAt = LocalDateTime.now()
        )
        return ResponseEntity.ok(service.create(account))
    }
    @GetMapping
    fun getAll(): ResponseEntity<List<AccountResponse>> =
        ResponseEntity.ok(service.getAll())

    @GetMapping("/{accountNumber}")
    fun getByAccountNumber(@PathVariable accountNumber: String): ResponseEntity<AccountResponse> {
        val result = service.getByAccountNumber(accountNumber)
        return if (result != null) ResponseEntity.ok(result)
        else ResponseEntity.notFound().build()
    }

}
