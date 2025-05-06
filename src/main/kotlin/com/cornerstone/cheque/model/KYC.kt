package com.cornerstone.cheque.model

import jakarta.persistence.*

@Entity
data class KYC(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    val user: User = User(),

    val name: String = "",
    var phone: String? = null
)