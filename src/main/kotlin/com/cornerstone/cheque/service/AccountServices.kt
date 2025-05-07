package com.cornerstone.cheque.service

import com.cornerstone.cheque.model.Account
import com.cornerstone.cheque.model.AccountRequest
import com.cornerstone.cheque.repo.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AccountService(
    private val repository: AccountRepository
) {
    fun create(account: Account): Account {
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
