package com.cornerstone.cheque.service

import com.cornerstone.cheque.model.*
import com.cornerstone.cheque.repo.AccountRepository
import com.cornerstone.cheque.repo.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class AccountService(
    private val repository: AccountRepository,
    private val userRepository: UserRepository
) {

    fun create(userId: Long, request: AccountRequest): AccountResponse {
        val user = userRepository.findById(userId).orElseThrow { RuntimeException("User not found") }

        if (repository.findByUser(user) != null) {
            throw Exception("User already has an account...")
        }

        val account = Account(
            accountNumber = generateUniqueAccountNumber(),
            user = user,
            accountType = request.accountType,
            createdAt = LocalDateTime.now()
        )

        val saved = repository.save(account)

        return toResponse(saved)
    }

    fun getById(id: Long): AccountResponse? {
        return repository.findById(id).orElse(null)?.let { toResponse(it) }
    }

    fun getByUserId(email: String): List<AccountResponse> {
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User not found")
        return repository.findAll()
            .filter { it.user?.id == user.id }
            .map { toResponse(it) }
    }

    fun getByAccountNumber(accountNumber: String): AccountResponse? {
        return repository.findByAccountNumber(accountNumber)?.let { toResponse(it) }
    }

    fun getAll(): List<AccountResponse> {
        return repository.findAll().map { toResponse(it) }
    }

     fun generateUniqueAccountNumber(): String {
        var accountNumber: String
        do {
            accountNumber = (1000000000L..9999999999L).random().toString()
        } while (repository.existsByAccountNumber(accountNumber))
        return accountNumber
    }

    private fun toResponse(account: Account): AccountResponse {
        return AccountResponse(
            accountNumber = account.accountNumber,
            userId = account.user?.id ?: 0,
            balance = account.balance,
            spendingLimit = account.spendingLimit,
            accountType = account.accountType,
            createdAt = account.createdAt
        )
    }
}
