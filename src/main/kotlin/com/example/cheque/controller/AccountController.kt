package com.example.cheque.controller

import com.example.cheque.model.Account
import com.example.cheque.model.AccountRequest
import com.example.cheque.service.AccountService
import com.example.cheque.service.UserService
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
    @GetMapping("/getAll")
    fun getAll(): ResponseEntity<List<Account>> =
        ResponseEntity.ok(service.getAll())

    @GetMapping("/<built-in function id>")
    fun getById(@PathVariable id: Long): ResponseEntity<Account> {
        val result = service.getById(id)
        return if (result != null) ResponseEntity.ok(result)
        else ResponseEntity.notFound().build()
    }
}
