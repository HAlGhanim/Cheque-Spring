package com.cornerstone.cheque.service

import com.cornerstone.cheque.model.PaymentLink
import com.cornerstone.cheque.repo.PaymentLinkRepository
import org.springframework.stereotype.Service

@Service
class PaymentLinkService(private val repo: PaymentLinkRepository) {

    fun getAll() = repo.findAll()

    fun getById(id: Long) = repo.findById(id)

    fun create(paymentLink: PaymentLink) = repo.save(paymentLink)

    fun delete(id: Long) = repo.deleteById(id)
}
