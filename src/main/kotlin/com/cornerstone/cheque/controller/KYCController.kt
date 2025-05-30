package com.cornerstone.cheque.controller

import com.cornerstone.cheque.model.KYC
import com.cornerstone.cheque.model.KYCRequest
import com.cornerstone.cheque.repo.UserRepository
import com.cornerstone.cheque.service.KYCService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/kyc")
class KYCController(
    private val service: KYCService,
    private val userRepository: UserRepository
) {
    @PreAuthorize("hasRole('USER','ADMIN')")
    @PostMapping
    fun create(@RequestBody request: KYCRequest, principal: Principal): ResponseEntity<KYC> {
        val user = userRepository.findByEmail(principal.name)
            ?: throw IllegalArgumentException("User not found")
        service.create(user.id!!, request.name, request.phone)
        return ResponseEntity.ok().build()
    }

    @PreAuthorize("hasRole('USER','ADMIN')")
    @GetMapping("/user")
    fun getByUserId(principal: Principal): ResponseEntity<KYC> {
        val user = userRepository.findByEmail(principal.name)
            ?: throw IllegalArgumentException("User not found")
        return ResponseEntity.ok(service.getByUserId(user.id!!) ?: throw IllegalArgumentException("KYC not found for user"))
    }
}