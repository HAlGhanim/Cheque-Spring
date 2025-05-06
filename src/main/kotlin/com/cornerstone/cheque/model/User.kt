package com.cornerstone.cheque.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var email: String= "",
    var password: String = "",
    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER
    )
enum class Role {
    USER,ADMIN
}