package com.cornerstone.cheque.service

import com.cornerstone.cheque.model.Transaction
import com.cornerstone.cheque.model.TransactionResponse
import com.cornerstone.cheque.repo.TransactionRepository
import com.cornerstone.cheque.repo.AccountRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class TransactionService(
    private val repository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val currencyService: CurrencyService //new
) {

    fun create(entity: Transaction): Transaction
//        repository.save(entity)
    {
        val converted = currencyService.convertToKWD(entity.amount, entity.currency)
        val updatedEntity = entity.copy(convertedAmount = converted)
        return repository.save(updatedEntity)
    } // new

    fun getById(id: Long): Transaction? =
        repository.findById(id).orElse(null)

    fun getAll(): List<Transaction> =
        repository.findAll()

    fun getByAccountNumber(accountNumber: String): List<TransactionResponse> {
        val account = accountRepository.findByAccountNumber(accountNumber)
            ?: throw IllegalArgumentException("Account not found")

        return repository.findAll()
            .filter {
                (it.senderAccount?.accountNumber == account.accountNumber) ||
                        (it.receiverAccount?.accountNumber == account.accountNumber)
            }
            .map { //new
                TransactionResponse(
                    id = it.id,
                    senderAccountNumber = it.senderAccount.accountNumber,
                    receiverAccountNumber = it.receiverAccount.accountNumber,
                    amount = it.amount,
                    currency = it.currency,
                    convertedAmount = it.convertedAmount,
                    createdAt = it.createdAt
                )

                }
            }
    }




