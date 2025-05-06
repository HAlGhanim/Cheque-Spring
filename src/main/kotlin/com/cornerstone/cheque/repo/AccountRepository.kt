package com.cornerstone.cheque.repo

import com.cornerstone.cheque.model.Account
import com.cornerstone.cheque.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, Long> {
    fun findByAccountNumber(accountNumber: String): Account?
}
