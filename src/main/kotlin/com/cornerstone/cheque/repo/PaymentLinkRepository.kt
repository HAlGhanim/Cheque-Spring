package com.cornerstone.cheque.repo

import com.cornerstone.cheque.model.PaymentLink
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentLinkRepository : JpaRepository<PaymentLink, Long>
