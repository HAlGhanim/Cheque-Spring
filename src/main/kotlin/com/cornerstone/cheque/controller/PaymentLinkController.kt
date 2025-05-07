package com.cornerstone.cheque.controller

import com.cornerstone.cheque.model.PaymentLink
import com.cornerstone.cheque.service.PaymentLinkService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/payment-links")
class PaymentLinkController(private val service: PaymentLinkService) {

    @GetMapping
    fun getAll() = ResponseEntity.ok(service.getAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) =
        service.getById(id).map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping
    fun create(@RequestBody link: PaymentLink) = ResponseEntity.ok(service.create(link))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}
