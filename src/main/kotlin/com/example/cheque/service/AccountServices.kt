package com.example.cheque.service

import com.example.cheque.model.Account
import com.example.cheque.repository.AccountRepository
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

    fun getById(id: Long): Account? = repository.findById(id).orElse(null)

    fun getAll(): List<Account> = repository.findAll()
}
