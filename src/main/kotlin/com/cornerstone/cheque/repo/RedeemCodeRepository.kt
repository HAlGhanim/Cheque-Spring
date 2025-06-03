package com.cornerstone.cheque.repo

import com.cornerstone.cheque.model.RedeemCode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface RedeemCodeRepository : JpaRepository<RedeemCode, Long> {
    fun findByCode(code: String): RedeemCode?
    fun countByUsedFalse(): Long // Active codes (unused)
    fun countByUsedTrue(): Long // Inactive codes (used)

    @Query("SELECT r FROM RedeemCode r LEFT JOIN FETCH r.user")
    fun findAllWithUser(): List<RedeemCode>
}