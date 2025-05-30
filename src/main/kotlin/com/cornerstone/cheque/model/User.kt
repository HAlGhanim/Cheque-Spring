package com.cornerstone.cheque.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var email: String= "",
    var password: String = "",
    @Enumerated(EnumType.STRING)

    @Column(nullable = false)
    var role: Role = Role.USER,

    @Column(nullable = false)
    var status: String = "Active",

    @Column(nullable = false)
    val joinedDate: LocalDateTime = LocalDateTime.now()

    )
enum class Role {
    USER,ADMIN
}
