package com.example.cheque.repository

import com.example.cheque.model.KYC
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface KYCRepository : JpaRepository<KYC, Long>
