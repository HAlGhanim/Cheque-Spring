package com.example.cheque.controller

import com.example.cheque.model.Transaction
import com.example.cheque.service.TransactionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/transactions")
class TransactionController(private val service: TransactionService) {

    @PostMapping
    fun create(@RequestBody entity: Transaction): ResponseEntity<Transaction> =
        ResponseEntity.ok(service.create(entity))

    @GetMapping
    fun getAll(): ResponseEntity<List<Transaction>> =
        ResponseEntity.ok(service.getAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Transaction> {
        val result = service.getById(id)
        return if (result != null) ResponseEntity.ok(result)
        else ResponseEntity.notFound().build()
    }
}
