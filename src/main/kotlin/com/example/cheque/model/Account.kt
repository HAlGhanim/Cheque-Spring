package com.example.cheque.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Account(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val accountNumber: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User?,

    val balance: Double,
    val spendingLimit: Int,
    val currency: String,
    @Enumerated(EnumType.STRING)
    var accountType: AccountType = AccountType.CUSTOMER,
    val createdAt: LocalDateTime
)

enum class AccountType {
    CUSTOMER, MARCHANT
}
