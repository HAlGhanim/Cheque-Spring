package com.cornerstone.cheque.model

import jakarta.persistence.*

@Entity
@Table(name = "kycs")
data class KYC(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToOne
    @JoinColumn(name = "user_id")
    val user: User?,

    var name: String = "",
    var phone: String? = null
)
data class KYCRequest(
    val userId: Long,
    val name: String,
    val phone: String?
)
data class KYCResponse (
    val name: String,
    val phone: String?
)