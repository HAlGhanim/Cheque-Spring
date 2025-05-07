package com.cornerstone.cheque.controller

import com.cornerstone.cheque.model.Invoice
import com.cornerstone.cheque.service.InvoiceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/invoices")
class InvoiceController(private val service: InvoiceService) {

    @GetMapping
    fun getAll() = ResponseEntity.ok(service.getAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) =
        service.getById(id).map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping
    fun create(@RequestBody invoice: Invoice) = ResponseEntity.ok(service.create(invoice))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}
