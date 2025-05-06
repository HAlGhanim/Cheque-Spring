package com.cornerstone.cheque.auth

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepo: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepo.findByEmail(username)
            ?: throw UsernameNotFoundException("User with email $username not found")

        return User.builder()
            .username(user.email)
            .password(user.password)
            .roles(user.role.name)
            .build()
    }
}