package com.cornerstone.cheque.service

import com.cornerstone.cheque.model.Invoice
import com.cornerstone.cheque.repo.InvoiceRepository
import org.springframework.stereotype.Service

@Service
class InvoiceService(private val repo: InvoiceRepository) {

    fun getAll() = repo.findAll()

    fun getById(id: Long) = repo.findById(id)

    fun create(invoice: Invoice) = repo.save(invoice)

    fun delete(id: Long) = repo.deleteById(id)
}
