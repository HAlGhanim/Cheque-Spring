package com.cornerstone.cheque.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "payment_links")
data class PaymentLink(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val uuid: String = UUID.randomUUID().toString(),

    @ManyToOne
    @JoinColumn(name = "account_number", nullable = false)
    val account: Account,

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    var transaction: Transaction? = null,

    @Column(nullable = false)
    val amount: BigDecimal,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    var status: String
)

data class PaymentLinkRequest(
    val accountNumber: String,
    val amount: BigDecimal,
    val description: String
)

data class PaymentLinkUseRequest(
    val recipientAccountNumber: String,
    )

