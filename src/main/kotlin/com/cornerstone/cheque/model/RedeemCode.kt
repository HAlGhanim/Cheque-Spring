package com.cornerstone.cheque.model

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "redeem_codes", uniqueConstraints = [UniqueConstraint(columnNames = ["code"])])
data class RedeemCode(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val code: String,

    @Column(nullable = false)
    val amount: BigDecimal,

    @Column(nullable = false)
    var used: Boolean = false
)