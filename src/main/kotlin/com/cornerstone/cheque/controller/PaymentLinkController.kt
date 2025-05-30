package com.cornerstone.cheque.controller

import com.cornerstone.cheque.model.PaymentLinkRequest
import com.cornerstone.cheque.service.PaymentLinkService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/payment-links")
class PaymentLinkController(
    private val service: PaymentLinkService
) {
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    fun create(@RequestBody request: PaymentLinkRequest, principal: Principal): ResponseEntity<PaymentLinkResponse> =
        ResponseEntity.ok(service.toResponse(service.create(request, principal.name)))

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/use/{uuid}")
    fun use(@PathVariable uuid: String, principal: Principal): ResponseEntity<Any> {
        return try {
            val result = service.usePaymentLink(uuid, principal.name)
            ResponseEntity.ok(service.toResponse(result))
        } catch (e: Exception) {
            ResponseEntity.status(400).body(mapOf("error" to e.message))
        }
    }
}