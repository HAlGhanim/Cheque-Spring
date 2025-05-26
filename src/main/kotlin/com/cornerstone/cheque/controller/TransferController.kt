package com.cornerstone.cheque.controller

import com.cornerstone.cheque.service.InvoiceRequest
import com.cornerstone.cheque.service.InvoiceResponse
import com.cornerstone.cheque.service.InvoiceService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/invoices")
class TransferController(private val invoiceService: InvoiceService) {

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    fun create(@RequestBody request: InvoiceRequest, principal: Principal): ResponseEntity<Any> {
        val response = invoiceService.create(request, principal.name)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun getInvoiceById(@PathVariable id: Long): ResponseEntity<InvoiceResponse> {
        val invoice = invoiceService.getById(id) ?: throw IllegalArgumentException("Invoice not found")
        return ResponseEntity.ok(invoice)
    }

    @GetMapping("/getAll")
    fun getAllInvoices(): ResponseEntity<List<InvoiceResponse>> =
        ResponseEntity.ok(invoiceService.getAll())

    @GetMapping("/user/{userId}")
    fun getByUserId(@PathVariable userId: Long): ResponseEntity<List<InvoiceResponse>> =
        ResponseEntity.ok(invoiceService.getByUserId(userId))

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/my")
    fun getMyInvoices(principal: Principal): ResponseEntity<List<InvoiceResponse>> =
        ResponseEntity.ok(invoiceService.getMyInvoices(principal.name))

    @GetMapping("/transaction/{transactionId}")
    fun getByTransactionId(@PathVariable transactionId: Long): ResponseEntity<List<InvoiceResponse>> =
        ResponseEntity.ok(invoiceService.getByTransactionId(transactionId))

    @GetMapping("/account/{accountNumber}")
    fun getByAccountNumber(@PathVariable accountNumber: String): ResponseEntity<List<InvoiceResponse>> =
        ResponseEntity.ok(invoiceService.getByAccountNumber(accountNumber))
}
