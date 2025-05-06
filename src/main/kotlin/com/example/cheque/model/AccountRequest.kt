package com.example.cheque.model

import java.time.LocalDateTime

data class AccountRequest(
    val accountNumber: String,
    val userId: Long,
    val balance: Double,
    val spendingLimit: Int,
    val currency: String,
    val accountType: AccountType,
    val createdAt: LocalDateTime
)
