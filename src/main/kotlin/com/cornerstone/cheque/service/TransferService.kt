package com.cornerstone.cheque.service

import com.cornerstone.cheque.model.Transfer
import com.cornerstone.cheque.model.Transaction
import com.cornerstone.cheque.model.transactionType
import com.cornerstone.cheque.repo.*
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.plus

@Service
class TransferService(
    private val transferRepository: TransferRepository,
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository
) {

    fun create(request: TransferRequest, userEmail: String): TransferResponse {

        val fromUser = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("User not found")

        val account = accountRepository.findByUser(fromUser)
            ?: throw IllegalArgumentException("Account not found")


        val receiverAccount = accountRepository.findByAccountNumber(request.receiverAccount)
            ?: throw IllegalArgumentException("Receiver account not found")

        val toUser = receiverAccount.user
            ?: throw IllegalArgumentException("Receiver account is not linked to any user")


        account.balance -= request.amount
        receiverAccount.balance += request.amount

        accountRepository.save(account)
        accountRepository.save(receiverAccount)

        val transaction = Transaction(
            senderAccount = account,
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

    fun getById(id: Long): TransferResponse? =
        transferRepository.findById(id).orElse(null)?.let { toResponse(it) }

    fun getAll(): List<TransferResponse> =
        transferRepository.findAll().map { toResponse(it) }

    fun getByUserId(userId: Long): List<TransferResponse> =
        transferRepository.findByFromUserIdOrToUserId(userId, userId).map { toResponse(it) }

    fun getMyInvoices(userEmail: String): List<TransferResponse> {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("User not found")
        return transferRepository.findByFromUserIdOrToUserId(user.id!!, user.id!!)
            .map { toResponse(it) }
    }

    fun getByTransactionId(transactionId: Long): List<TransferResponse> =
        transferRepository.findByTransactionId(transactionId).map { toResponse(it) }

    fun getByAccountNumber(accountNumber: String): List<TransferResponse> =
        transferRepository.findByTransaction_SenderAccount_AccountNumberOrTransaction_ReceiverAccount_AccountNumber(
            accountNumber, accountNumber
        ).map { toResponse(it) }

    private fun toResponse(transfer: Transfer): TransferResponse =
        TransferResponse(
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

data class TransferRequest(
    val receiverAccount: String,
    val amount: BigDecimal,
    val description: String
)

data class TransferResponse(
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
