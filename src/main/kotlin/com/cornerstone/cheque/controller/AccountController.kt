package com.cornerstone.cheque.controller

import com.cornerstone.cheque.model.AccountRequest
import com.cornerstone.cheque.model.AccountResponse
import com.cornerstone.cheque.service.AccountService
import com.cornerstone.cheque.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/accounts")
class AccountController(
    private val accountService: AccountService,
    private val userService: UserService
) {
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/create")
    fun createAccount(@RequestBody request: AccountRequest, principal: Principal): ResponseEntity<Any> {
        val user = userService.findByEmail(principal.name)
            ?: throw IllegalStateException("User not authenticated")
        val userId = user.id ?: throw IllegalStateException("User ID missing")
        return ResponseEntity.ok(accountService.create(userId, request))
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/my")
    fun getMyAccounts(principal: Principal): ResponseEntity<List<AccountResponse>> {
        val user = userService.findByEmail(principal.name)
            ?: throw IllegalStateException("User not authenticated")
        return ResponseEntity.ok(accountService.getByUserId(user.email))
    }
}