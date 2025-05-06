package com.cornerstone.cheque.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.cornerstone.cheque.model.KYC
import com.cornerstone.cheque.service.KYCService

@RestController
@RequestMapping("/api/kyc")
class KYCController(private val service: KYCService) {

    @PostMapping
    fun create(@RequestBody entity: KYC): ResponseEntity<KYC> =
        ResponseEntity.ok(service.create(entity))

    @GetMapping
    fun getAll(): ResponseEntity<List<KYC>> =
        ResponseEntity.ok(service.getAll())
        
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<KYC> {
        val result = service.getById(id)
        return if (result != null) ResponseEntity.ok(result)
        else ResponseEntity.notFound().build()
    }
}
