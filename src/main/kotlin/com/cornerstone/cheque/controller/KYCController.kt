package com.cornerstone.cheque.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/kyc")
class KYCController(private val service: KYCService) {
    @PostMapping
    fun create(@RequestBody entity: KYC): ResponseEntity<KYC>
}