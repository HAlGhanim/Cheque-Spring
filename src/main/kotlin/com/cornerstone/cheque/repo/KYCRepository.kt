package com.cornerstone.cheque.repo

import com.cornerstone.cheque.model.KYC
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface KYCRepository : JpaRepository<KYC, Long> {}
