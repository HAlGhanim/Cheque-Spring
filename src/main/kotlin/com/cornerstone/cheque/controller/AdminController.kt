package com.cornerstone.cheque.controller

import com.cornerstone.cheque.model.*
import com.cornerstone.cheque.repo.AccountRepository
import com.cornerstone.cheque.repo.UserRepository
import com.cornerstone.cheque.service.*
import jakarta.validation.constraints.NotNull
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
class AdminController(
    private val userService: UserService,
    private val redeemService: RedeemService,
    private val transactionService: TransactionService,
    private val kycService: KYCService,
    private val userRepository: UserRepository,
    private val accountService: AccountService,
    private val accountRepository: AccountRepository,
    private val paymentLinkService: PaymentLinkService,
    private val transferService: TransferService
) {

    data class DashboardStats(
        val totalUsers: Long,
        val totalTransactions: Int,
        val growthPercentage: Double,
        val lastUpdated: LocalDateTime
    )

    @GetMapping("/dashboard")
    fun getDashboardStats(): ResponseEntity<DashboardStats> {
        val totalUsers = userService.getTotalUsers()
        val totalTransactions = transactionService.getTotalTransactionAmount()
        val growthPercentage = calculateGrowth()
        val lastUpdated = LocalDateTime.now()

        return ResponseEntity.ok(
            DashboardStats(totalUsers, totalTransactions, growthPercentage, lastUpdated)
        )
    }

    @GetMapping("/users")
    fun getUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) role: String?
    ): ResponseEntity<Any> {
        val users = userService.getUsers(page, size, role)
        return ResponseEntity.ok(users)
    }

    @GetMapping("/users/{userId}")
    fun getUserById(@PathVariable userId: Long): ResponseEntity<User> =
        ResponseEntity.ok(userService.getUserById(userId))

    @DeleteMapping("/users/{userId}")
    fun deleteUser(@PathVariable userId: Long): ResponseEntity<Void> {
        val user = userService.getUserById(userId)
        if (user.role == Role.ADMIN) throw IllegalStateException("Cannot delete admin users")
        userService.deleteUser(userId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/users/{userId}/reset-password")
    fun resetPassword(@PathVariable userId: Long): ResponseEntity<String> =
        ResponseEntity.ok(userService.resetPassword(userId))

    @PostMapping("/users/{userId}/suspend")
    fun suspendUser(@PathVariable userId: Long): ResponseEntity<Void> {
        userService.suspendUser(userId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/redeem/active/count")
    fun getActiveCodeCount(): ResponseEntity<Any> {
        val count = redeemService.countActiveCodes()
        return ResponseEntity.ok(mapOf("activeCodes" to count))
    }

    @PostMapping("/redeem/generate")
    fun generateCode(@RequestBody request: RedeemRequest): ResponseEntity<Any> {
        val code = redeemService.generate(request.amount)
        return ResponseEntity.ok(mapOf("code" to code.code, "amount" to code.amount))
    }

    @GetMapping("/kyc/getAll")
    fun getAllKYC(): ResponseEntity<List<KYC>> =
        ResponseEntity.ok(kycService.getAll())

    @GetMapping("/kyc/{id}")
    fun getKYCById(@PathVariable id: Long): ResponseEntity<KYC> =
        ResponseEntity.ok(kycService.getById(id) ?: throw IllegalArgumentException("KYC not found"))

    @GetMapping("/accounts/getAll")
    fun getAllAccounts(): ResponseEntity<List<AccountResponse>> =
        ResponseEntity.ok(accountService.getAll())

    @GetMapping("/accounts/{accountNumber}")
    fun getAccountByNumber(@PathVariable accountNumber: String): ResponseEntity<AccountResponse> {
        val result = accountService.getByAccountNumber(accountNumber)
            ?: throw IllegalArgumentException("Account not found")
        return ResponseEntity.ok(result)
    }

    @GetMapping("/transactions/getAll")
    fun getAllTransactions(): ResponseEntity<List<TransactionResponse>> =
        ResponseEntity.ok(transactionService.getAll().map {
            TransactionResponse(
                id = it.id,
                senderAccountNumber = it.senderAccount.accountNumber,
                receiverAccountNumber = it.receiverAccount.accountNumber,
                amount = it.amount,
                createdAt = it.createdAt
            )
        })

    @GetMapping("/payment-links/getAll")
    fun getAllPaymentLinks() =
        ResponseEntity.ok(paymentLinkService.getAll().map { paymentLinkService.toResponse(it) })

    @GetMapping("/payment-links/{id}")
    fun getPaymentLinkById(@PathVariable id: Long): ResponseEntity<PaymentLinkResponse> =
        ResponseEntity.ok(paymentLinkService.getResponseById(id))

    @DeleteMapping("/payment-links/{id}")
    fun deletePaymentLink(@PathVariable id: Long): ResponseEntity<Void> {
        paymentLinkService.delete(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/transfers/getAll")
    fun getAllTransfers(): ResponseEntity<List<TransferResponse>> =
        ResponseEntity.ok(transferService.getAll())

    private fun calculateGrowth(): Double = 24.8
}

data class RedeemRequest(
    @NotNull
    val amount: BigDecimal
)

data class PaymentLinkResponse(
    val id: Long,
    val accountNumber: String,
    val amount: BigDecimal,
    val description: String,
    val status: String,
    val transactionId: Long?,
    val uuid: String
)

data class TransactionResponse(
    val id: Long,
    val senderAccountNumber: String,
    val receiverAccountNumber: String,
    val amount: BigDecimal,
    val createdAt: LocalDateTime
)