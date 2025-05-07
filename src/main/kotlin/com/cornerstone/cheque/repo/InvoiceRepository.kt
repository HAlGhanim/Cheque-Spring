package com.cornerstone.cheque.repo

import com.cornerstone.cheque.model.Invoice
import org.springframework.data.jpa.repository.JpaRepository

interface InvoiceRepository : JpaRepository<Invoice, Long>
