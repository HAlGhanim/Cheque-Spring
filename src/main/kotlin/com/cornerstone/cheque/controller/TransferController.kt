package com.cornerstone.cheque.controller

import com.cornerstone.cheque.service.TransferRequest
import com.cornerstone.cheque.service.TransferResponse
import com.cornerstone.cheque.service.TransferService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/transfer")
class TransferController(private val invoiceService: TransferService) {

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    fun create(@RequestBody request: TransferRequest, principal: Principal): ResponseEntity<Any> {
        val response = invoiceService.create(request, principal.name)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun getInvoiceById(@PathVariable id: Long): ResponseEntity<TransferResponse> {
        val invoice = invoiceService.getById(id) ?: throw IllegalArgumentException("Invoice not found")
        return ResponseEntity.ok(invoice)
    }

    @GetMapping("/getAll")
    fun getAllInvoices(): ResponseEntity<List<TransferResponse>> =
        ResponseEntity.ok(invoiceService.getAll())

    @GetMapping("/user/{userId}")
    fun getByUserId(@PathVariable userId: Long): ResponseEntity<List<TransferResponse>> =
        ResponseEntity.ok(invoiceService.getByUserId(userId))

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/my")
    fun getMyInvoices(principal: Principal): ResponseEntity<List<TransferResponse>> =
        ResponseEntity.ok(invoiceService.getMyInvoices(principal.name))

    @GetMapping("/transaction/{transactionId}")
    fun getByTransactionId(@PathVariable transactionId: Long): ResponseEntity<List<TransferResponse>> =
        ResponseEntity.ok(invoiceService.getByTransactionId(transactionId))

    @GetMapping("/account/{accountNumber}")
    fun getByAccountNumber(@PathVariable accountNumber: String): ResponseEntity<List<TransferResponse>> =
        ResponseEntity.ok(invoiceService.getByAccountNumber(accountNumber))
}
