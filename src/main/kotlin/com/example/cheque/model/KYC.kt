package com.example.cheque.model

import jakarta.persistence.*

@Entity
data class KYC(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToOne
    @JoinColumn(name = "user_id")
    val user: User? = null,

    var name: String = "",
    var phone: String? = null
)
