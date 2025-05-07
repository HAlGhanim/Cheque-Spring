package com.cornerstone.cheque.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    @Column(name = "account_number", nullable = false)
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

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDate
)

enum class AccountType {
    CUSTOMER, MERCHANT
}
data class AccountRequest(
    val accountNumber: String,
    val userId: Long,
    var balance: BigDecimal,
    val spendingLimit: Int?,
    val currency: String,
    val accountType: AccountType,
    val createdAt: LocalDate
)
