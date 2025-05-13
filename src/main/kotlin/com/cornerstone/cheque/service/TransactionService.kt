package com.cornerstone.cheque.service

import com.cornerstone.cheque.model.Account
import com.cornerstone.cheque.model.AccountResponse
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
    private val accountRepository: AccountRepository
) {

    fun create(entity: Transaction): Transaction =
        repository.save(entity)

    fun getById(id: Long): Transaction? =
        repository.findById(id).orElse(null)

    fun getAll(): List<Transaction> =
        repository.findAll()

    fun deposit(amount: BigDecimal, account: Account): Account {
        account.balance += amount
        return accountRepository.save(account)
    }

    fun withdraw(amount: BigDecimal, account: Account): Account {
        if (account.balance < amount) throw IllegalArgumentException("Insufficient balance")
        account.balance -= amount
        return accountRepository.save(account)
    }


    fun getByAccountNumber(accountNumber: String): List<TransactionResponse> {
        val account = accountRepository.findByAccountNumber(accountNumber)
            ?: throw IllegalArgumentException("Account not found")

        return repository.findAll()
            .filter {
                (it.senderAccount?.accountNumber == account.accountNumber) ||
                        (it.receiverAccount?.accountNumber == account.accountNumber)
            }
            .mapNotNull {
                val sender = it.senderAccount
                val receiver = it.receiverAccount
                if (sender != null && receiver != null) {
                    TransactionResponse(
                        id = it.id,
                        senderAccountNumber = sender.accountNumber,
                        receiverAccountNumber = receiver.accountNumber,
                        amount = it.amount,
                        createdAt = it.createdAt
                    )
                } else {
                    null
                }
            }
    }
}



