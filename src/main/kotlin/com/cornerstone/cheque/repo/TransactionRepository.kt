package com.cornerstone.cheque.repo

import com.cornerstone.cheque.model.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    @Query("SELECT SUM(t.amount) FROM Transaction t")
    fun getTotalTransactionAmount(): Int?
}