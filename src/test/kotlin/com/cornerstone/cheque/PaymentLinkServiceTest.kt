package com.cornerstone.cheque

import com.cornerstone.cheque.controller.PaymentLinkResponse
import com.cornerstone.cheque.model.*
import com.cornerstone.cheque.repo.AccountRepository
import com.cornerstone.cheque.repo.PaymentLinkRepository
import com.cornerstone.cheque.repo.TransactionRepository
import com.cornerstone.cheque.repo.UserRepository
import com.cornerstone.cheque.service.PaymentLinkService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import kotlin.test.*

class PaymentLinkServiceTest {


    private lateinit var paymentLinkRepository: PaymentLinkRepository
    private lateinit var accountRepository: AccountRepository
    private lateinit var userRepository: UserRepository
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var service: PaymentLinkService

    private val user = User(
        email = "newuser7@gmail.com",
        password = "pass123",
        role = Role.USER
    )

    private lateinit var account: Account
    private lateinit var transaction: Transaction

    @BeforeEach
    fun setup() {
        paymentLinkRepository = mock(PaymentLinkRepository::class.java)
        accountRepository = mock(AccountRepository::class.java)
        transactionRepository = mock(TransactionRepository::class.java)
        service = PaymentLinkService(
            paymentLinkRepository, accountRepository, transactionRepository,
            userRepository
        )

        account = Account(
            accountNumber = "7738384767373",
            user = user,
            balance = BigDecimal("500.000"),
            spendingLimit = 300,
            accountType = AccountType.CUSTOMER,
            createdAt = LocalDateTime.now()
        )

        transaction = Transaction(
            senderAccount = account,
            receiverAccount = account,
            amount = BigDecimal("150.000"),
            createdAt = LocalDateTime.now()
        )
    }

    @Test
    fun `should return PaymentLinkResponse by ID`() {
        val paymentLink = PaymentLink(
            id = 1,
            account = account,
            transaction = transaction,
            amount = BigDecimal("150.000"),
            description = "Test Link",
            status = "PENDING"
        )

        `when`(paymentLinkRepository.findById(1)).thenReturn(Optional.of(paymentLink))

        val result: PaymentLinkResponse = service.getResponseById(1)

        assertEquals("Test Link", result.description)
        assertEquals(BigDecimal("150.000"), result.amount)
        assertEquals("PENDING", result.status)
    }

    @Test
    fun `should throw if payment link not found`() {
        `when`(paymentLinkRepository.findById(99L)).thenReturn(Optional.empty())

        val ex = assertFailsWith<IllegalArgumentException> {
            service.getResponseById(99L)
        }

        assertEquals("Payment link not found", ex.message)
    }
}