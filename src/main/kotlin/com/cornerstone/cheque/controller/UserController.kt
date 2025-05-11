package com.cornerstone.cheque.controller

import com.cornerstone.cheque.model.User
import com.cornerstone.cheque.repo.UserRepository
import com.cornerstone.cheque.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class UserController(
    private val service: UserService,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @PostMapping("/auth/register")
    fun create(@RequestBody entity: User): ResponseEntity<Any> {
        if (userRepository.findByEmail(entity.email) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("error" to "Email already exists"))
        }
        val hashedPassword = passwordEncoder.encode(entity.password)
        val newUser = entity.copy(password = hashedPassword)
        return ResponseEntity.ok(service.create(newUser))
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    fun getAll(): ResponseEntity<List<User>> = ResponseEntity.ok(service.getAll())

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<User> {
        val result = service.getById(id) ?: throw IllegalArgumentException("User not found")
        return ResponseEntity.ok(result)
    }
}
