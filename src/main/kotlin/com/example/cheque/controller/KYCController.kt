package com.example.cheque.controller

import com.example.cheque.model.KYC
import com.example.cheque.service.KYCService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/kycs")
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
