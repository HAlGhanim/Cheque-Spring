package com.cornerstone.cheque.controller

import com.cornerstone.cheque.model.Account
import com.cornerstone.cheque.model.AccountResponse
import com.cornerstone.cheque.model.Transaction
import com.cornerstone.cheque.model.TransactionRequest
import com.cornerstone.cheque.repo.AccountRepository
import com.cornerstone.cheque.service.TransactionService
import com.cornerstone.cheque.model.AccountType
import com.cornerstone.cheque.model.DepositRequest
import com.cornerstone.cheque.model.TransactionResponse
import com.cornerstone.cheque.service.AccountService
import com.cornerstone.cheque.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.time.LocalDateTime

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    private val service: TransactionService,
    private val accountRepository: AccountRepository,
    private val userService: UserService,
    private val accountService: AccountService
) {
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    fun create(@RequestBody request: TransactionRequest): ResponseEntity<TransactionResponse> {
        val sender = accountRepository.findByAccountNumber(request.senderAccount)
            ?: throw IllegalArgumentException("Sender account not found")

        val receiver = accountRepository.findByAccountNumber(request.receiverAccount)
            ?: throw IllegalArgumentException("Receiver account not found")

        if (sender.balance < request.amount)
            throw IllegalArgumentException("Insufficient balance")

        if (sender.accountType == AccountType.CUSTOMER) {
            val spendingLimit = sender.spendingLimit
            if (spendingLimit == null || request.amount > spendingLimit.toBigDecimal()) {
                throw IllegalArgumentException("Exceeded the spending limit")
            }
        }

        sender.balance -= request.amount
        receiver.balance += request.amount

        accountRepository.save(sender)
        accountRepository.save(receiver)

        val transaction = Transaction(
            senderAccount = sender,
            receiverAccount = receiver,
            amount = request.amount,
            createdAt = LocalDateTime.now()
        )
        val saved = service.create(transaction)

        return ResponseEntity.ok(
            TransactionResponse(
                id = saved.id,
                senderAccountNumber = sender.accountNumber,
                receiverAccountNumber = receiver.accountNumber,
                amount = saved.amount,
                createdAt = saved.createdAt
            )
        )
    }

    @GetMapping("/getAll")
    fun getAll(): ResponseEntity<List<TransactionResponse>> =
        ResponseEntity.ok(service.getAll().map {
            TransactionResponse(
                id = it.id,
                senderAccountNumber = it.senderAccount.accountNumber,
                receiverAccountNumber = it.receiverAccount.accountNumber,
                amount = it.amount,
                createdAt = it.createdAt
            )
        })

    @PostMapping("/deposit")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    fun deposit(@RequestBody request: DepositRequest, principal: Principal): ResponseEntity<AccountResponse> {
        val user = userService.findByUsername(principal.name)
            ?: throw IllegalStateException("User not authenticated")

        val account = accountService.getByUserId(user.email).firstOrNull()
            ?: throw IllegalArgumentException("User does not have an account")

        val updated = service.deposit(request.amount, accountRepository.findByAccountNumber(account.accountNumber)!!)
        return ResponseEntity.ok(accountService.getByAccountNumber(updated.accountNumber))
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    fun withdraw(@RequestBody request: DepositRequest, principal: Principal): ResponseEntity<AccountResponse> {
        val user = userService.findByUsername(principal.name)
            ?: throw IllegalStateException("User not authenticated")

        val account = accountService.getByUserId(user.email).firstOrNull()
            ?: throw IllegalArgumentException("User does not have an account")

        val updated = service.withdraw(request.amount, accountRepository.findByAccountNumber(account.accountNumber)!!)
        return ResponseEntity.ok(accountService.getByAccountNumber(updated.accountNumber))
    }

}