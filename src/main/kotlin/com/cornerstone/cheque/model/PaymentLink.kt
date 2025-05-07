package com.cornerstone.cheque.model

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "payment_links")
data class PaymentLink(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "account_number", nullable = false)
    val account: Account,

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    val transaction: Transaction? = null,

    @Column(nullable = false)
    val amount: BigDecimal,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    val status: String
)
