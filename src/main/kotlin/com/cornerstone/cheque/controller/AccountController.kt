package com.cornerstone.cheque.controller

import com.cornerstone.cheque.model.AccountRequest
import com.cornerstone.cheque.model.AccountResponse
import com.cornerstone.cheque.repo.AccountRepository
import com.cornerstone.cheque.service.AccountService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/accounts")
class AccountController(
    private val service: AccountService,
) {

    @PostMapping
    fun createAccount(@RequestBody request: AccountRequest, principal: Principal): ResponseEntity<out Any?> {
        val userId = principal.name.toLong()
        val account = service.create(userId, request)
        return ResponseEntity.ok(account)
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

    @GetMapping("/my")
    fun getMyAccounts(principal: Principal): ResponseEntity<List<AccountResponse>> {
        val userId = principal.name.toLong()
        val accounts = service.getByUserId(userId)
        return ResponseEntity.ok(accounts)
    }

}
