package com.cornerstone.cheque.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Transaction(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    val senderAccount: Account? = null,

    @ManyToOne
    @JoinColumn(name = "receiver_account_id")
    val receiverAccount: Account? = null,

    var amount: Double = 0.0,
    // for the multicurrency
    var currency: String = "KWD",
    var convertedAmount: Double = 0.0,

    var createdAt: LocalDateTime? = null
)
