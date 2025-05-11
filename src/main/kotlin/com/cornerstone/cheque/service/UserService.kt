package com.cornerstone.cheque.service

import com.cornerstone.cheque.repo.UserRepository
import org.springframework.stereotype.Service
import com.cornerstone.cheque.model.User

@Service
class UserService(private val repository: UserRepository) {
    fun findByUsername(username: String): User? {
        return repository.findByEmail(username)
    }

    fun create (entity: User): User = repository.save(entity)

    fun getById (id: Long): User = repository.findById(id).orElse(null)

    fun getAll(): List<User> = repository.findAll()

}
