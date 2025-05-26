package com.cornerstone.cheque.controller

import com.cornerstone.cheque.model.PaymentLinkRequest
import com.cornerstone.cheque.model.PaymentLinkUseRequest
import com.cornerstone.cheque.service.*
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/payment-links")
class PaymentLinkController(
    private val service: PaymentLinkService
) {
    @GetMapping("/getAll")
    fun getAll() = ResponseEntity.ok(service.getAll().map { service.toResponse(it) })

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<PaymentLinkResponse> =
        ResponseEntity.ok(service.getResponseById(id))

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    fun create(@RequestBody request: PaymentLinkRequest): ResponseEntity<PaymentLinkResponse> =
        ResponseEntity.ok(service.toResponse(service.create(request)))

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/use/{uuid}")
    fun use(@PathVariable uuid: String, @RequestBody request: PaymentLinkUseRequest): ResponseEntity<Any> {
        return try {
            val result = service.usePaymentLink(uuid, request)
            ResponseEntity.ok(result)
        } catch (e: Exception) {
            ResponseEntity.status(400).body(mapOf("error" to e.message))
        }
    }
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}


data class PaymentLinkResponse(
    val id: Long,
    val accountNumber: String,
    val amount: BigDecimal,
    val description: String,
    val status: String,
    val transactionId: Long?
)

