package com.cornerstone.cheque.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    @Column(name = "account_number")
    val accountNumber: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User?,

    var balance: BigDecimal,

    @Column(name = "spending_limit")
    val spendingLimit: Int?,

    @Column(nullable = false)
    val currency: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    var accountType: AccountType = AccountType.CUSTOMER,

    @Column(name = "created_at")
    val createdAt: LocalDateTime  = LocalDateTime.now()
)

enum class AccountType {
    CUSTOMER, MERCHANT
}
data class AccountRequest(
    val userId: Long,
    var balance: BigDecimal,
    val spendingLimit: Int?,
    val currency: String,
    val accountType: AccountType,
)
data class AccountResponse(
    val accountNumber: String,
    val userId: Long,
    val balance: BigDecimal,
    val spendingLimit: Int?,
    val currency: String,
    val accountType: AccountType,
    val createdAt: LocalDateTime
)