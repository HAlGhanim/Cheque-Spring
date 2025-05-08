package com.cornerstone.cheque.service

import com.cornerstone.cheque.model.Account
import com.cornerstone.cheque.model.AccountResponse
import com.cornerstone.cheque.model.AccountType
import com.cornerstone.cheque.repo.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AccountService(
    private val repository: AccountRepository
) {
    fun create(account: Account): Account {
        val spendingLimit = account.spendingLimit

        when (account.accountType) {
            AccountType.CUSTOMER -> require(spendingLimit != null && spendingLimit > 0) {
                "Customer accounts must have a spending limit greater than 0"
            }
            AccountType.MERCHANT -> require(spendingLimit == null || spendingLimit == 0) {
                "Merchant accounts cannot have a spending limit"
            }
        }

        return repository.save(account)
    }

    fun getById(id: Long): AccountResponse? {
        val account = repository.findById(id).orElse(null) ?: return null

        return AccountResponse(
            accountNumber = account.accountNumber,
            userId = account.user?.id ?: 0,
            balance = account.balance,
            spendingLimit = account.spendingLimit,
            currency = account.currency,
            accountType = account.accountType,
            createdAt = account.createdAt
        )
    }

    fun getByAccountNumber(accountNumber: String): AccountResponse? {
        val account = repository.findByAccountNumber(accountNumber) ?: return null

        return AccountResponse(
            accountNumber = account.accountNumber,
            userId = account.user?.id ?: 0,
            balance = account.balance,
            spendingLimit = account.spendingLimit,
            currency = account.currency,
            accountType = account.accountType,
            createdAt = account.createdAt
        )
    }
    fun getAll(): List<AccountResponse> {
        return repository.findAll().map {
            AccountResponse(
                accountNumber = it.accountNumber,
                userId = it.user?.id ?: 0,
                balance = it.balance,
                spendingLimit = it.spendingLimit,
                currency = it.currency,
                accountType = it.accountType,
                 createdAt = it.createdAt
            )
        }
    }
    fun generateUniqueAccountNumber(accountRepository: AccountRepository): String {
        var accountNumber: String
        do {
            accountNumber = (1000000000L..9999999999L).random().toString() // 10 digits
        } while (accountRepository.existsByAccountNumber(accountNumber))
        return accountNumber
    }
}
