package com.cornerstone.cheque.auth.jwt

import com.cornerstone.cheque.repo.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val jwtService: JwtService
) {

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest): ResponseEntity<AuthResponse> {
        val authToken = UsernamePasswordAuthenticationToken(request.email, request.password)
        val authentication = authenticationManager.authenticate(authToken)

        if (authentication.isAuthenticated) {
            val user = userRepository.findByEmail(request.email)
                ?: throw UsernameNotFoundException("User not found")

            val token = jwtService.generateToken(request.email)
            return ResponseEntity.ok(AuthResponse(token))
        } else {
            throw UsernameNotFoundException("Invalid credentials")
        }
    }
}

data class AuthRequest(val email: String, val password: String)
data class AuthResponse(val token: String)
