package com.example.cheque.controller

import com.example.cheque.model.User
import com.example.cheque.repository.UserRepository
import com.example.cheque.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/users")
class UserController(private val service: UserService,
                     private val userRepository: UserRepository
) {

    @PostMapping
    fun create(@RequestBody entity: User): ResponseEntity<out Any?> {
        if (userRepository.findByEmail(entity.email) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists.")
        }
//        val hashedPassword = passwordEncoder.encode(entity.password)
//        val newUser = entity.copy(password = hashedPassword)

        return ResponseEntity.ok(service.create(entity))

    }
    @GetMapping
    fun getAll(): ResponseEntity<List<User>> =
        ResponseEntity.ok(service.getAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<User> {
        val result = service.getById(id)
        return if (result != null) ResponseEntity.ok(result)
        else ResponseEntity.notFound().build()
    }
}
