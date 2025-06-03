package com.cornerstone.cheque.controller

import com.cornerstone.cheque.service.RedeemService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/redeem")
class RedeemController(
    private val redeemService: RedeemService
) {

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/use/{code}")
    fun useCode(@PathVariable code: String, principal: Principal): ResponseEntity<Any> {
        val message = redeemService.redeem(code, principal.name)
        return ResponseEntity.ok(mapOf("message" to message))
    }
}