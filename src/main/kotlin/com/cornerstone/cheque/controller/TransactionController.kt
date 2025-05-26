package com.cornerstone.cheque.controller

import com.cornerstone.cheque.model.Transaction
import com.cornerstone.cheque.model.TransactionRequest
import com.cornerstone.cheque.repo.AccountRepository
import com.cornerstone.cheque.service.TransactionService
import com.cornerstone.cheque.model.AccountType
import com.cornerstone.cheque.model.TransactionResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    private val service: TransactionService) {

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

    @GetMapping("/account/{accountNumber}")
    fun getByAccountNumber(@PathVariable accountNumber: String): ResponseEntity<List<TransactionResponse>> =
        ResponseEntity.ok(service.getByAccountNumber(accountNumber))
}