package com.cornerstone.cheque.service

import com.cornerstone.cheque.model.Transaction
import com.cornerstone.cheque.repo.TransactionRepository
import com.cornerstone.cheque.repo.AccountRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class TransactionService(
    private val repository: TransactionRepository,
    private val accountRepository: AccountRepository
) {

    fun create(entity: Transaction): Transaction =
        repository.save(entity)

    fun getById(id: Long): Transaction? =
        repository.findById(id).orElse(null)

    fun getAll(): List<Transaction> =
        repository.findAll()

    fun getByAccountNumber(accountNumber: String): List<TransactionResponse> {
        val account = accountRepository.findByAccountNumber(accountNumber)
            ?: throw IllegalArgumentException("Account not found")

        return repository.findAll().filter {
            it.senderAccount.accountNumber == account.accountNumber || it.receiverAccount.accountNumber == account.accountNumber
        }.map {
            TransactionResponse(
                id = it.id,
                senderAccountNumber = it.senderAccount.accountNumber,
                receiverAccountNumber = it.receiverAccount.accountNumber,
                amount = it.amount,
                createdAt = it.createdAt
            )
        }
    }

}

data class TransactionResponse(
    val id: Long,
    val senderAccountNumber: String,
    val receiverAccountNumber: String,
    val amount: Double,
    val createdAt: LocalDate
)

