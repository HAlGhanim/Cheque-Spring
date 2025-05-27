package com.cornerstone.cheque.repo

import com.cornerstone.cheque.model.RedeemCode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RedeemCodeRepository : JpaRepository<RedeemCode, Long> {
    fun findByCode(code: String): RedeemCode?
}