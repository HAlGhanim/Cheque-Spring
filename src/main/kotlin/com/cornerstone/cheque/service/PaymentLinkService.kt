package com.cornerstone.cheque.service

import com.cornerstone.cheque.controller.PaymentLinkResponse
import com.cornerstone.cheque.model.PaymentLink
import com.cornerstone.cheque.model.PaymentLinkRequest
import com.cornerstone.cheque.model.PaymentLinkUseRequest
import com.cornerstone.cheque.model.Transaction
import com.cornerstone.cheque.model.transactionType
import com.cornerstone.cheque.repo.AccountRepository
import com.cornerstone.cheque.repo.PaymentLinkRepository
import com.cornerstone.cheque.repo.TransactionRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class PaymentLinkService(
    private val paymentLinkRepository: PaymentLinkRepository,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) {

    fun getResponseById(id: Long): PaymentLinkResponse {
        val link = paymentLinkRepository.findById(id).orElseThrow { IllegalArgumentException("Payment link not found") }
        return toResponse(link)
    }

    fun toResponse(paymentLink: PaymentLink): PaymentLinkResponse {
        return PaymentLinkResponse(
            id = paymentLink.id!!,
            accountNumber = paymentLink.account.accountNumber,
            amount = paymentLink.amount,
            description = paymentLink.description,
            status = paymentLink.status,
            transactionId = paymentLink.transaction?.id
        )
    }

    fun getAll(): List<PaymentLink> = paymentLinkRepository.findAll()

    fun getById(id: Long) = paymentLinkRepository.findById(id)

    fun create(request: PaymentLinkRequest): PaymentLink {
        val account = accountRepository.findByAccountNumber(request.accountNumber)
            ?: throw IllegalArgumentException("Account not found")

        val paymentLink = PaymentLink(
            uuid = UUID.randomUUID().toString(),
            account = account,
            amount = request.amount,
            description = request.description,
            status = "ACTIVE"
        )

        return paymentLinkRepository.save(paymentLink)
    }

    fun usePaymentLink(uuid: String, request: PaymentLinkUseRequest): PaymentLink {
        val paymentLink = paymentLinkRepository.findByUuid(uuid)
            ?: throw IllegalArgumentException("Payment link not found")

        if (paymentLink.status != "ACTIVE") {
            throw IllegalStateException("Payment link is not active")
        }

        val recipientAccount = accountRepository.findByAccountNumber(request.recipientAccountNumber)
            ?: throw IllegalArgumentException("Recipient account not found")

        val senderAccount = paymentLink.account

        if (senderAccount.accountNumber == recipientAccount.accountNumber) {
            throw IllegalArgumentException("Sender and recipient accounts cannot be the same")
        }

        if (senderAccount.balance < paymentLink.amount) {
            throw IllegalArgumentException("Sender account has insufficient balance")
        }
        senderAccount.balance -= paymentLink.amount
        recipientAccount.balance += paymentLink.amount

        accountRepository.save(senderAccount)
        accountRepository.save(recipientAccount)


        val transaction = Transaction(
            senderAccount = senderAccount,
            receiverAccount = recipientAccount,
            amount = paymentLink.amount,
            createdAt = LocalDateTime.now(),
            transType = transactionType.PAYMENT_LINK
        )

        val savedTransaction = transactionRepository.save(transaction)


        paymentLink.transaction = savedTransaction
        paymentLink.status = "USED"

        return paymentLinkRepository.save(paymentLink)
    }

    fun delete(id: Long) = paymentLinkRepository.deleteById(id)
}
