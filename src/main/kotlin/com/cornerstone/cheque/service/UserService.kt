package com.cornerstone.cheque.service

import com.cornerstone.cheque.model.Role
import com.cornerstone.cheque.model.User
import com.cornerstone.cheque.repo.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val userRepository: UserRepository) {

    fun create(user: User): User {
        return userRepository.save(user)
    }

    fun getAll(): List<User> {
        return userRepository.findAll()
    }

    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun getTotalUsers(): Long {
        return userRepository.count()
    }

    fun getUsers(page: Int, size: Int, role: String?): Any {
        val pageable = PageRequest.of(page, size)
        return if (role != null) {
            userRepository.findByRole(Role.valueOf(role.uppercase()), pageable)
        } else {
            userRepository.findAll(pageable).content
        }
    }

    fun getUserById(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }
    }

    fun deleteUser(userId: Long) {
        userRepository.deleteById(userId)
    }

    fun resetPassword(userId: Long): String {
        val user = getUserById(userId)
        val newPassword = UUID.randomUUID().toString().substring(0, 8)
        user.password = newPassword // In production, hash the password
        userRepository.save(user)
        return newPassword
    }

    fun suspendUser(userId: Long) {
        val user = getUserById(userId)
        user.status = if (user.status == "Active") "Suspended" else "Active"
        userRepository.save(user)
    }

}