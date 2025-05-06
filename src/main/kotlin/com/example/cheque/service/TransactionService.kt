package com.example.cheque.service

import com.example.cheque.model.Transaction
import com.example.cheque.repository.TransactionRepository
import org.springframework.stereotype.Service

@Service
class TransactionService(private val repository: TransactionRepository) {

    fun create(entity: Transaction): Transaction = repository.save(entity)

    fun getById(id: Long): Transaction? = repository.findById(id).orElse(null)

    fun getAll(): List<Transaction> = repository.findAll()
}
