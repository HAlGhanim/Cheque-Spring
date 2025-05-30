package com.cornerstone.cheque.controller

import com.cornerstone.cheque.service.TransferRequest
import com.cornerstone.cheque.service.TransferResponse
import com.cornerstone.cheque.service.TransferService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/transfer")
class TransferController(private val invoiceService: TransferService) {

    @PreAuthorize("hasRole('USER','ADMIN')")
    @PostMapping
    fun create(@RequestBody request: TransferRequest, principal: Principal): ResponseEntity<Any> {
        val response = invoiceService.create(request, principal.name)
        return ResponseEntity.ok(response)
    }

    @PreAuthorize("hasRole('USER','ADMIN')")
    @GetMapping("/my")
    fun getMyInvoices(principal: Principal): ResponseEntity<List<TransferResponse>> =
        ResponseEntity.ok(invoiceService.getMyInvoices(principal.name))
}