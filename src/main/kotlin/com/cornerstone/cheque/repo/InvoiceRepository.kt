package com.cornerstone.cheque.repo

import com.cornerstone.cheque.model.Invoice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InvoiceRepository : JpaRepository<Invoice, Long> {

    fun findByFromUserIdOrToUserId(fromUserId: Long, toUserId: Long): List<Invoice>

    fun findByTransactionId(transactionId: Long): List<Invoice>

    fun findByTransaction_SenderAccount_AccountNumberOrTransaction_ReceiverAccount_AccountNumber(
        senderAccountNumber: String,
        receiverAccountNumber: String
    ): List<Invoice>
}
