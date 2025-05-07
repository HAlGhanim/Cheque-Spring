package com.cornerstone.cheque.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "accounts")
data class Account(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val accountNumber: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User?,

    var balance: Double,
    val spendingLimit: Int,
    val currency: String,
    @Enumerated(EnumType.STRING)
    var accountType: AccountType = AccountType.CUSTOMER,
    val createdAt: LocalDate
)

enum class AccountType {
    CUSTOMER, MARCHANT
}
data class AccountRequest(
    val accountNumber: String,
    val userId: Long,
    var balance: Double,
    val spendingLimit: Int,
    val currency: String,
    val accountType: AccountType,
    val createdAt: LocalDate
)
