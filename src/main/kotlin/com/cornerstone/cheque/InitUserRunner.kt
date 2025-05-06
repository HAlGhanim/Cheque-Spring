package com.cornerstone.cheque


import com.cornerstone.cheque.model.Role
import com.cornerstone.cheque.model.User
import com.cornerstone.cheque.repo.UserRepository
import org.springframework.boot.runApplication
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.password.PasswordEncoder

class InitUserRunner {
    @Bean
    fun initUsers(userRepoo: UserRepository, passwordEncoder: PasswordEncoder) = CommandLineRunner {
        val user = User(
            email = "testuser@gmail.com",
            password = passwordEncoder.encode("password123"),
            role = Role.USER
        )
        if (userRepoo.findByEmail(user.email) == null) {
            println("Creating user with email ${user.email}")
            userRepoo.save(user)
        } else  {
            println("Email ${user.email} already exists")
        }
    }
}

fun main(args: Array<String>) {
    runApplication<InitUserRunner>(*args)
}