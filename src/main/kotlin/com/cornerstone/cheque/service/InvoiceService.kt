package com.cornerstone.cheque.service

import com.cornerstone.cheque.model.*
import com.cornerstone.cheque.repo.AccountRepository
import com.cornerstone.cheque.repo.InvoiceRepository
import com.cornerstone.cheque.repo.TransactionRepository
import com.cornerstone.cheque.repo.UserRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class InvoiceService(
    private val invoiceRepository: InvoiceRepository,
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository
) {

    fun create(request: InvoiceRequest): InvoiceResponse {
        val fromUser = userRepository.findById(request.fromUserId).orElseThrow {
            IllegalArgumentException("From user not found")
        }

        val toUser = userRepository.findById(request.toUserId).orElseThrow {
            IllegalArgumentException("To user not found")
        }

        val senderAccount = accountRepository.findByAccountNumber(request.senderAccount)
            ?: throw IllegalArgumentException("Sender account not found")

        val receiverAccount = accountRepository.findByAccountNumber(request.receiverAccount)
            ?: throw IllegalArgumentException("Receiver account not found")

        // ✅ Validate sender account belongs to fromUser
        if (senderAccount.user?.id != fromUser.id) {
            throw IllegalArgumentException("Sender account does not belong to from user")
        }

        // ✅ Validate receiver account belongs to toUser
        if (receiverAccount.user?.id != toUser.id) {
            throw IllegalArgumentException("Receiver account does not belong to to user")
        }

        val transaction = Transaction(
            senderAccount = senderAccount,
            receiverAccount = receiverAccount,
            amount = request.amount,
            createdAt = LocalDate.now()
        )
        val savedTransaction = transactionRepository.save(transaction)

        val invoice = Invoice(
            fromUser = fromUser,
            toUser = toUser,
            amount = request.amount,
            transaction = savedTransaction,
            description = request.description,
            createdAt = LocalDate.now()
        )
        val savedInvoice = invoiceRepository.save(invoice)

        return toResponse(savedInvoice)
    }

    fun getById(id: Long): InvoiceResponse? {
        return invoiceRepository.findById(id).orElse(null)?.let { toResponse(it) }
    }

    fun getAll(): List<InvoiceResponse> {
        return invoiceRepository.findAll().map { toResponse(it) }
    }

    fun getByUserId(userId: Long): List<InvoiceResponse> {
        return invoiceRepository.findByFromUserIdOrToUserId(userId, userId).map { toResponse(it) }
    }

    fun getByTransactionId(transactionId: Long): List<InvoiceResponse> {
        return invoiceRepository.findByTransactionId(transactionId).map { toResponse(it) }
    }

    fun getByAccountNumber(accountNumber: String): List<InvoiceResponse> {
        return invoiceRepository
            .findByTransaction_SenderAccount_AccountNumberOrTransaction_ReceiverAccount_AccountNumber(
                accountNumber, accountNumber
            ).map { toResponse(it) }
    }

    private fun toResponse(invoice: Invoice): InvoiceResponse {
        return InvoiceResponse(
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
}

data class InvoiceRequest(
    val fromUserId: Long,
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
    val createdAt: LocalDate
)
