package com.cornerstone.cheque.auth

import com.cornerstone.cheque.repo.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("User not found")

        return User.builder()
            .username(user.id.toString())
            .password(user.password)
            .roles(user.role.name)
            .build()
    }

    fun loadUserById(id: Long): UserDetails {
        val user = userRepository.findById(id).orElseThrow { UsernameNotFoundException("User not found") }

        return User.builder()
            .username(user.id.toString())
            .password(user.password)
            .roles(user.role.name)
            .build()
    }
}