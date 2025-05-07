package com.cornerstone.cheque.service

import com.cornerstone.cheque.model.Account
import com.cornerstone.cheque.model.AccountRequest
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

    fun getById(id: Long): AccountRequest? {
        val account = repository.findById(id).orElse(null) ?: return null

        return AccountRequest(
            accountNumber = account.accountNumber,
            userId = account.user?.id ?: 0,
            balance = account.balance,
            spendingLimit = account.spendingLimit,
            currency = account.currency,
            accountType = account.accountType,
            createdAt = account.createdAt
        )
    }

    fun getByAccountNumber(accountNumber: String): AccountRequest? {
        val account = repository.findByAccountNumber(accountNumber) ?: return null

        return AccountRequest(
            accountNumber = account.accountNumber,
            userId = account.user?.id ?: 0,
            balance = account.balance,
            spendingLimit = account.spendingLimit,
            currency = account.currency,
            accountType = account.accountType,
            createdAt = account.createdAt
        )
    }

    fun getAll(): List<AccountRequest> {
        return repository.findAll().map {
            AccountRequest(
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
}
