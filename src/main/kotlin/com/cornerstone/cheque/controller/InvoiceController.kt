package com.cornerstone.cheque.controller

import com.cornerstone.cheque.service.InvoiceRequest
import com.cornerstone.cheque.service.InvoiceResponse
import com.cornerstone.cheque.service.InvoiceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/invoices")
class InvoiceController(private val invoiceService: InvoiceService) {

    @PostMapping
    fun create(@RequestBody request: InvoiceRequest, principal: Principal): ResponseEntity<Any> {
        return try {
            val response = invoiceService.create(request, principal.name)
            ResponseEntity.ok(response)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/{id}")
    fun getInvoiceById(@PathVariable id: Long): ResponseEntity<InvoiceResponse> =
        invoiceService.getById(id)?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()

    @GetMapping
    fun getAllInvoices(): ResponseEntity<List<InvoiceResponse>> =
        ResponseEntity.ok(invoiceService.getAll())

    @GetMapping("/user/{userId}")
    fun getByUserId(@PathVariable userId: Long): ResponseEntity<List<InvoiceResponse>> =
        ResponseEntity.ok(invoiceService.getByUserId(userId))

    @GetMapping("/my")
    fun getMyInvoices(principal: Principal): ResponseEntity<List<InvoiceResponse>> {
        return ResponseEntity.ok(invoiceService.getMyInvoices(principal.name))
    }


    @GetMapping("/transaction/{transactionId}")
    fun getByTransactionId(@PathVariable transactionId: Long): ResponseEntity<List<InvoiceResponse>> =
        ResponseEntity.ok(invoiceService.getByTransactionId(transactionId))

    @GetMapping("/account/{accountNumber}")
    fun getByAccountNumber(@PathVariable accountNumber: String): ResponseEntity<List<InvoiceResponse>> =
        ResponseEntity.ok(invoiceService.getByAccountNumber(accountNumber))
}
