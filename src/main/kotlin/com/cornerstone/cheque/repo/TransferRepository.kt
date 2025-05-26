package com.cornerstone.cheque.repo

import com.cornerstone.cheque.model.Transfer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransferRepository : JpaRepository<Transfer, Long> {

    fun findByFromUserIdOrToUserId(fromUserId: Long, toUserId: Long): List<Transfer>

    fun findByTransactionId(transactionId: Long): List<Transfer>

    fun findByTransaction_SenderAccount_AccountNumberOrTransaction_ReceiverAccount_AccountNumber(
        senderAccountNumber: String,
        receiverAccountNumber: String
    ): List<Transfer>
}
