package com.cornerstone.cheque.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "transaction")
data class Transaction(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    val senderAccount: Account,

    @ManyToOne
    val receiverAccount: Account,

    val amount: Double,
    val createdAt: LocalDate
)

data class TransactionRequest(
    val senderAccount: String,
    val receiverAccount: String,
    val amount: Double
)