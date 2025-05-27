package com.cornerstone.cheque.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "transactions")
data class Transaction(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "sender_account_number")
    val senderAccount: Account,

    @ManyToOne
    @JoinColumn(name = "receiver_account_number")
    val receiverAccount: Account,

    @Column(nullable = false)
    val amount: BigDecimal,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    val transType: transactionType = transactionType.NONE)

data class TransactionRequest(
    val senderAccount: String,
    val receiverAccount: String,
    val amount: BigDecimal,
)
data class TransactionResponse(
    val id: Long,
    val senderAccountNumber: String,
    val receiverAccountNumber: String,
    val amount: BigDecimal,
    val createdAt: LocalDateTime
)
enum class transactionType {
    PAYMENT_LINK, TRANSFER, NONE
}

data class DepositRequest(
    val amount: BigDecimal
)
