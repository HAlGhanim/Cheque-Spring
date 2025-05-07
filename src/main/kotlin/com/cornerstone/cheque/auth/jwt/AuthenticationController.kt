package com.cornerstone.cheque.auth.jwt

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val jwtService: JwtService
) {
    @PostMapping("/login")
    fun login(@RequestBody authRequest: AuthenticationRequest): AuthenticationResponse {
        val authToken = UsernamePasswordAuthenticationToken(authRequest.email, authRequest.password)
        val authentication = authenticationManager.authenticate(authToken)

        if (authentication.isAuthenticated) {
            val userDetails = userDetailsService.loadUserByUsername(authRequest.email)
            val token = jwtService.generateToken(userDetails.username)
            return AuthenticationResponse(token)

        } else {
            throw UsernameNotFoundException("Invalid user request!")
        }
    }
        }
        data class AuthenticationRequest(
            val email: String,
            val password: String
        )

        data class AuthenticationResponse(
            val token: String
        )
