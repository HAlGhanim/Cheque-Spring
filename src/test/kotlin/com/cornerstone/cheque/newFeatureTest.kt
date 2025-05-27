package com.cornerstone.cheque

import com.cornerstone.cheque.model.*
import com.cornerstone.cheque.repo.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [ChequeApplication::class]
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NewFeatureTest {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var accountRepository: AccountRepository

    private val objectMapper = jacksonObjectMapper()

    @Test
    @WithMockUser(roles = ["ADMIN"])

//    @WithMockUser(roles = ["USER"])
    fun `should create a new transaction`() {
        val sender = accountRepository.save(
            Account(
                accountNumber = "111222333",
                user = userRepository.save(User(email = "sender77@gmail.com", password = "pass", role = Role.USER)),
                balance = BigDecimal("1000"),
                spendingLimit = 500,
                accountType = AccountType.CUSTOMER,
                createdAt = LocalDateTime.now()
            )
        )

        val receiver = accountRepository.save(
            Account(
                accountNumber = "444555666",
                user = userRepository.save(User(email = "receiver@gmail.com", password = "pass", role = Role.USER)),
                balance = BigDecimal("500"),
                spendingLimit = null,
                accountType = AccountType.MERCHANT,
                createdAt = LocalDateTime.now()
            )
        )

        val request = mapOf(
            "senderAccount" to sender.accountNumber,
            "receiverAccount" to receiver.accountNumber,
            "amount" to BigDecimal("200"),
            "currency" to "USD"
        )

        val result = mockMvc.post("/api/transactions") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }

        result.andExpect {
            status { isOk() }
            jsonPath("$.senderAccountNumber") { value(sender.accountNumber) }
            jsonPath("$.receiverAccountNumber") { value(receiver.accountNumber) }
            jsonPath("$.amount") { value(200) }
            jsonPath("$.currency") { value("USD") }
            jsonPath("$.convertedAmount", notNullValue())
        }
    }
}
