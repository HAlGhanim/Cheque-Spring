package com.cornerstone.cheque.controller

import com.cornerstone.cheque.service.InvoiceRequest
import com.cornerstone.cheque.service.InvoiceResponse
import com.cornerstone.cheque.service.InvoiceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/invoices")
class InvoiceController(
    private val invoiceService: InvoiceService
) {

    @PostMapping
    fun create(@RequestBody request: InvoiceRequest): ResponseEntity<Any> {
        return try {
            val response = invoiceService.create(request)
            ResponseEntity.ok(response)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to e.message))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("error" to "Unexpected error occurred"))
        }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Any> {
        val response = invoiceService.getById(id)
        return if (response != null) {
            ResponseEntity.ok(response)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to "Invoice not found"))
        }
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<InvoiceResponse>> {
        val responses = invoiceService.getAll()
        return ResponseEntity.ok(responses)
    }

    @GetMapping("/user/{userId}")
    fun getByUserId(@PathVariable userId: Long): ResponseEntity<List<InvoiceResponse>> {
        return ResponseEntity.ok(invoiceService.getByUserId(userId))
    }

    @GetMapping("/transaction/{transactionId}")
    fun getByTransactionId(@PathVariable transactionId: Long): ResponseEntity<List<InvoiceResponse>> {
        return ResponseEntity.ok(invoiceService.getByTransactionId(transactionId))
    }

    @GetMapping("/account/{accountNumber}")
    fun getByAccountNumber(@PathVariable accountNumber: String): ResponseEntity<List<InvoiceResponse>> {
        return ResponseEntity.ok(invoiceService.getByAccountNumber(accountNumber))
    }
}
