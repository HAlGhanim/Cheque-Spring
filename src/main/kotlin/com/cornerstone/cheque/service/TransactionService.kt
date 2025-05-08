package com.cornerstone.cheque.service

import com.cornerstone.cheque.model.Transaction
import com.cornerstone.cheque.repo.TransactionRepository
import org.springframework.stereotype.Service

@Service
class TransactionService(private val repository: TransactionRepository,
    private val currencyService: CurrencyService) {

    //fun create(entity: Transaction): Transaction = repository.save(entity)

    fun create(entity: Transaction): Transaction {
        val converted = currencyService.convertToKWD(entity.amount, entity.currency)
        val updatedEntity = entity.copy(convertedAmount = converted)
        return repository.save(updatedEntity)
    }

    fun getById(id: Long): Transaction? = repository.findById(id).orElse(null)

    fun getAll(): List<Transaction> = repository.findAll()
}
