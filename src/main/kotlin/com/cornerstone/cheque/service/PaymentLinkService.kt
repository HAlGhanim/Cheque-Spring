package com.cornerstone.cheque.service

import com.cornerstone.cheque.controller.PaymentLinkResponse
import com.cornerstone.cheque.model.PaymentLink
import com.cornerstone.cheque.model.PaymentLinkRequest
import com.cornerstone.cheque.model.PaymentLinkUseRequest
import com.cornerstone.cheque.model.Transaction
import com.cornerstone.cheque.repo.AccountRepository
import com.cornerstone.cheque.repo.PaymentLinkRepository
import com.cornerstone.cheque.repo.TransactionRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class PaymentLinkService(
    private val paymentLinkRepository: PaymentLinkRepository,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) {

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
            account = account,
            amount = request.amount,
            description = request.description,
            status = "ACTIVE"
        )

        return paymentLinkRepository.save(paymentLink)
    }

    fun usePaymentLink(id: Long, request: PaymentLinkUseRequest): PaymentLink {
        val paymentLink = paymentLinkRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Payment link not found") }

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

        // Deduct + Add
        senderAccount.balance -= paymentLink.amount
        recipientAccount.balance += paymentLink.amount

        accountRepository.save(senderAccount)
        accountRepository.save(recipientAccount)

        // Create transaction
        val transaction = Transaction(
            senderAccount = senderAccount,
            receiverAccount = recipientAccount,
            amount = paymentLink.amount,
            createdAt = LocalDate.now()
        )
        val savedTransaction = transactionRepository.save(transaction)

        // Update payment link
        paymentLink.transaction = savedTransaction
        paymentLink.status = "USED"

        return paymentLinkRepository.save(paymentLink)
    }

    fun delete(id: Long) = paymentLinkRepository.deleteById(id)
}
