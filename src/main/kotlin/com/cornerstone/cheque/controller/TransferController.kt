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
class TransferController(private val transferService: TransferService) {

    @PreAuthorize("hasRole('USER','ADMIN')")
    @PostMapping
    fun create(@RequestBody request: TransferRequest, principal: Principal): ResponseEntity<Any> {
        val response = transferService.create(request, principal.name)
        return ResponseEntity.ok(response)
    }

    @PreAuthorize("hasRole('USER','ADMIN')")
    @GetMapping("/my")
    fun getMyTransfers(principal: Principal): ResponseEntity<List<TransferResponse>> =
        ResponseEntity.ok(transferService.getMyTransfers(principal.name))
}