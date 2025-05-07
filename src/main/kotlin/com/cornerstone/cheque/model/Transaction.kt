package com.cornerstone.cheque.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "transactions")
data class Transaction(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "sender_account_id", referencedColumnName = "account_number")
    val senderAccount: Account,

    @ManyToOne
    @JoinColumn(name = "receiver_account_id", referencedColumnName = "account_number")
    val receiverAccount: Account,

    val amount: BigDecimal,
    val createdAt: LocalDate
)

data class TransactionRequest(
    val senderAccount: String,
    val receiverAccount: String,
    val amount: BigDecimal
)

