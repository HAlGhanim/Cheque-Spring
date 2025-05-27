package com.cornerstone.cheque.controller

import com.cornerstone.cheque.service.RedeemService
import jakarta.validation.constraints.NotNull
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.security.Principal

data class RedeemRequest(
    @NotNull
    val amount: BigDecimal
)

@RestController
@RequestMapping("/api/redeem")
class RedeemController(
    private val redeemService: RedeemService
) {

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/generate")
    fun generateCode(@RequestBody request: RedeemRequest): ResponseEntity<Any> {
        val code = redeemService.generate(request.amount)
        return ResponseEntity.ok(mapOf("code" to code.code, "amount" to code.amount))
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/use/{code}")
    fun useCode(@PathVariable code: String, principal: Principal): ResponseEntity<Any> {
        val message = redeemService.redeem(code, principal.name)
        return ResponseEntity.ok(mapOf("message" to message))
    }
}