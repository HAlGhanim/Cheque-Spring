package com.cornerstone.cheque.repo

import com.cornerstone.cheque.model.Role
import com.cornerstone.cheque.model.User
import org.hibernate.mapping.List
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.domain.Page
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findByRole(role: Role, pageable: PageRequest): Page<User>}

