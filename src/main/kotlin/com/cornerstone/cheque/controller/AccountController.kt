package com.cornerstone.cheque.controller

import com.cornerstone.cheque.model.Account
import com.cornerstone.cheque.model.AccountRequest
import com.cornerstone.cheque.service.AccountService
import com.cornerstone.cheque.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/accounts")
class AccountController(private val service: AccountService,
                        private val repository: UserService) {

    @PostMapping("/create")
    fun createAccount(@RequestBody request: AccountRequest): ResponseEntity<Account> {
        val user = repository.getById(request.userId)

        val account = Account(
            accountNumber = request.accountNumber,
            user = user,
            balance = request.balance,
            spendingLimit = request.spendingLimit,
            currency = request.currency,
            accountType = request.accountType,
            createdAt = request.createdAt
        )
        return ResponseEntity.ok(service.create(account))
    }
    @GetMapping
    fun getAll(): ResponseEntity<List<AccountRequest>> =
        ResponseEntity.ok(service.getAll())

    @GetMapping("/{accountNumber}")
    fun getByAccountNumber(@PathVariable accountNumber: String): ResponseEntity<AccountRequest> {
        val result = service.getByAccountNumber(accountNumber)
        return if (result != null) ResponseEntity.ok(result)
        else ResponseEntity.notFound().build()
    }

}
