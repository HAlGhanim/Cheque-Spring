package com.cornerstone.cheque.controller

import com.cornerstone.cheque.model.PaymentLinkRequest
import com.cornerstone.cheque.model.PaymentLinkUseRequest
import com.cornerstone.cheque.service.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/api/payment-links")
class PaymentLinkController(
    private val service: PaymentLinkService
) {

    @GetMapping
    fun getAll() = ResponseEntity.ok(service.getAll().map { service.toResponse(it) })

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) =
        service.getById(id).map { ResponseEntity.ok(service.toResponse(it)) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping
    fun create(@RequestBody request: PaymentLinkRequest): ResponseEntity<PaymentLinkResponse> {
        val paymentLink = service.create(request)
        return ResponseEntity.ok(service.toResponse(paymentLink))
    }

    @PostMapping("/{id}/use")
    fun use(@PathVariable id: Long, @RequestBody request: PaymentLinkUseRequest): ResponseEntity<PaymentLinkResponse> {
        return try {
            val result = service.usePaymentLink(id, request)
            ResponseEntity.ok(service.toResponse(result))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
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

