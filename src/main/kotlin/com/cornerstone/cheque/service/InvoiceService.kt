package com.cornerstone.cheque.service

import com.cornerstone.cheque.model.Invoice
import com.cornerstone.cheque.model.Transaction
import com.cornerstone.cheque.repo.*
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class InvoiceService(
    private val invoiceRepository: InvoiceRepository,
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository
) {

    fun create(request: InvoiceRequest, userId: String): InvoiceResponse {
        val fromUserId = userId.toLong()
        val fromUser = userRepository.findById(fromUserId).orElseThrow { IllegalArgumentException("From user not found") }

        val toUser = userRepository.findById(request.toUserId).orElseThrow {
            IllegalArgumentException("To user not found")
        }

        val senderAccount = accountRepository.findByAccountNumber(request.senderAccount)
            ?: throw IllegalArgumentException("Sender account not found")

        val receiverAccount = accountRepository.findByAccountNumber(request.receiverAccount)
            ?: throw IllegalArgumentException("Receiver account not found")

        val transaction = Transaction(
            senderAccount = senderAccount,
            receiverAccount = receiverAccount,
            amount = request.amount,
            createdAt = LocalDateTime.now()
        )

        val savedTransaction = transactionRepository.save(transaction)

        val invoice = Invoice(
            fromUser = fromUser,
            toUser = toUser,
            amount = request.amount,
            transaction = savedTransaction,
            description = request.description,
            createdAt = LocalDateTime.now()
        )

        return toResponse(invoiceRepository.save(invoice))
    }

    fun getById(id: Long): InvoiceResponse? =
        invoiceRepository.findById(id).orElse(null)?.let { toResponse(it) }

    fun getAll(): List<InvoiceResponse> =
        invoiceRepository.findAll().map { toResponse(it) }

    fun getByUserId(userId: Long): List<InvoiceResponse> =
        invoiceRepository.findByFromUserIdOrToUserId(userId, userId).map { toResponse(it) }

    // ✅ /my endpoint (Principal.id → Long → findById)
    fun getMyInvoices(userIdAsString: String): List<InvoiceResponse> {
        val userId = userIdAsString.toLong()
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("User not found") }

        return invoiceRepository.findByFromUserIdOrToUserId(user.id!!, user.id!!)
            .map { toResponse(it) }
    }

    fun getByTransactionId(transactionId: Long): List<InvoiceResponse> =
        invoiceRepository.findByTransactionId(transactionId).map { toResponse(it) }

    fun getByAccountNumber(accountNumber: String): List<InvoiceResponse> =
        invoiceRepository.findByTransaction_SenderAccount_AccountNumberOrTransaction_ReceiverAccount_AccountNumber(
            accountNumber, accountNumber
        ).map { toResponse(it) }

    private fun toResponse(invoice: Invoice): InvoiceResponse =
        InvoiceResponse(
            id = invoice.id!!,
            fromUserId = invoice.fromUser.id!!,
            toUserId = invoice.toUser.id!!,
            senderAccountNumber = invoice.transaction.senderAccount.accountNumber,
            receiverAccountNumber = invoice.transaction.receiverAccount.accountNumber,
            amount = invoice.amount,
            transactionId = invoice.transaction.id,
            description = invoice.description,
            createdAt = invoice.createdAt
        )
}

data class InvoiceRequest(
    val toUserId: Long,
    val senderAccount: String,
    val receiverAccount: String,
    val amount: BigDecimal,
    val description: String
)

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
