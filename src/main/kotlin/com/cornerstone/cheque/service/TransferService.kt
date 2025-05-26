package com.cornerstone.cheque.service

import com.cornerstone.cheque.model.Transfer
import com.cornerstone.cheque.model.Transaction
import com.cornerstone.cheque.model.transactionType
import com.cornerstone.cheque.repo.*
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class InvoiceService(
    private val transferRepository: TransferRepository,
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository
) {

    fun create(request: InvoiceRequest, userId: String): InvoiceResponse {
        val fromUser = userRepository.findByEmail(userId)
            ?: throw IllegalArgumentException("From user not found")

        val senderAccount = accountRepository.findByAccountNumber(request.senderAccount)
            ?: throw IllegalArgumentException("Sender account not found")

        if (senderAccount.user?.id != fromUser.id) {
            throw IllegalArgumentException("You do not own the sender account")
        }

        val receiverAccount = accountRepository.findByAccountNumber(request.receiverAccount)
            ?: throw IllegalArgumentException("Receiver account not found")

        val toUser = receiverAccount.user
            ?: throw IllegalArgumentException("Receiver account is not linked to any user")

        if (request.senderAccount == request.receiverAccount) {
            throw IllegalArgumentException("Sender and receiver accounts must be different")
        }


        val transaction = Transaction(
            senderAccount = senderAccount,
            receiverAccount = receiverAccount,
            amount = request.amount,
            createdAt = LocalDateTime.now(),
            transType = transactionType.TRANSFER
        )

        val savedTransaction = transactionRepository.save(transaction)

        val transfer = Transfer(
            fromUser = fromUser,
            toUser = toUser,
            amount = request.amount,
            transaction = savedTransaction,
            description = request.description,
            createdAt = LocalDateTime.now()
        )

        return toResponse(transferRepository.save(transfer))
    }

    fun getById(id: Long): InvoiceResponse? =
        transferRepository.findById(id).orElse(null)?.let { toResponse(it) }

    fun getAll(): List<InvoiceResponse> =
        transferRepository.findAll().map { toResponse(it) }

    fun getByUserId(userId: Long): List<InvoiceResponse> =
        transferRepository.findByFromUserIdOrToUserId(userId, userId).map { toResponse(it) }

    fun getMyInvoices(userEmail: String): List<InvoiceResponse> {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("User not found")
        return transferRepository.findByFromUserIdOrToUserId(user.id!!, user.id!!)
            .map { toResponse(it) }
    }

    fun getByTransactionId(transactionId: Long): List<InvoiceResponse> =
        transferRepository.findByTransactionId(transactionId).map { toResponse(it) }

    fun getByAccountNumber(accountNumber: String): List<InvoiceResponse> =
        transferRepository.findByTransaction_SenderAccount_AccountNumberOrTransaction_ReceiverAccount_AccountNumber(
            accountNumber, accountNumber
        ).map { toResponse(it) }

    private fun toResponse(transfer: Transfer): InvoiceResponse =
        InvoiceResponse(
            id = transfer.id!!,
            fromUserId = transfer.fromUser.id!!,
            toUserId = transfer.toUser.id!!,
            senderAccountNumber = transfer.transaction.senderAccount.accountNumber,
            receiverAccountNumber = transfer.transaction.receiverAccount.accountNumber,
            amount = transfer.amount,
            transactionId = transfer.transaction.id,
            description = transfer.description,
            createdAt = transfer.createdAt
        )
}

data class InvoiceRequest(
    val senderAccount: String,
    val receiverAccount: String,
    val amount: BigDecimal,
    val description: String
)

enum class InvoiceType {
    LINK, TRANSACTION
}

data class InvoiceResponse(
    val id: Long,
    val fromUserId: Long,
    val toUserId: Long,
    val senderAccountNumber: String,
    val receiverAccountNumber: String,
    val amount: BigDecimal,
    val transactionId: Long,
    val description: String,
    val createdAt: LocalDateTime
)
