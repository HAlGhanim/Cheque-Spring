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
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository) {



    fun getById(id: Long): Transaction? =
        transactionRepository.findById(id).orElse(null)

    fun getAll(): List<Transaction> =
        transactionRepository.findAll()

    fun deposit(amount: BigDecimal, account: Account): Account {
        account.balance += amount
        return accountRepository.save(account)
    }

    fun withdraw(amount: BigDecimal, account: Account): Account {
        if (account.balance < amount) throw IllegalArgumentException("Insufficient balance")
        account.balance -= amount
        return accountRepository.save(account)
    }

    fun getTotalTransactionAmount(): Int {
        return transactionRepository.getTotalTransactionAmount() ?: 0
    }

    fun getTotalTransactionCount(): Long {
        return transactionRepository.count()
    }


    fun getByAccountNumber(accountNumber: String): List<TransactionResponse> {
        val account = accountRepository.findByAccountNumber(accountNumber)
            ?: throw IllegalArgumentException("Account not found")

        return transactionRepository.findAll()
            .filter {
                (it.senderAccount?.accountNumber == account.accountNumber) ||
                        (it.receiverAccount?.accountNumber == account.accountNumber)
            }
            .map {
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




