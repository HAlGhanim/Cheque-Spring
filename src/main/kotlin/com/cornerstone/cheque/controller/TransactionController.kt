package com.cornerstone.cheque.controller

import com.cornerstone.cheque.model.Transaction
import com.cornerstone.cheque.model.TransactionRequest
import com.cornerstone.cheque.repo.AccountRepository
import com.cornerstone.cheque.service.TransactionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/transactions")
class TransactionController(private val service: TransactionService,
                                  private val accountRepository: AccountRepository) {

    @PostMapping
    fun create(@RequestBody request: TransactionRequest): ResponseEntity<Any> {
        return try {
            val sender = accountRepository.findByAccountNumber(request.senderAccount)
                ?: return ResponseEntity.badRequest().body(mapOf("error" to "Sender account not found"))

            val receiver = accountRepository.findByAccountNumber(request.receiverAccount)
                ?: return ResponseEntity.badRequest().body(mapOf("error" to "Receiver account not found"))

            if (sender.balance < request.amount)
                return ResponseEntity.badRequest().body(mapOf("error" to "Insufficient balance"))

            if (request.amount > sender.spendingLimit)
                return ResponseEntity.badRequest().body(mapOf("error" to "Exceeded the Limit"))

            sender.balance -= request.amount
            receiver.balance += request.amount

            accountRepository.save(sender)
            accountRepository.save(receiver)

            val transaction = Transaction(
                senderAccount = sender,
                receiverAccount = receiver,
                amount = request.amount,
                createdAt = LocalDate.now()
            )

            val savedTransaction = service.create(transaction)

            val response = TransactionRequest(
                senderAccount = sender.accountNumber,
                receiverAccount = receiver.accountNumber,
                amount = savedTransaction.amount,
            )

            ResponseEntity.ok(response)

        } catch (ex: Exception) {
            ex.printStackTrace()
            ResponseEntity.status(500).body(mapOf("error" to "Internal server error", "details" to ex.message))
        }
    }
    @GetMapping
    fun getAll(): ResponseEntity<List<Transaction>> =
        ResponseEntity.ok(service.getAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Transaction> {
        val result = service.getById(id)
        return if (result != null) ResponseEntity.ok(result)
        else ResponseEntity.notFound().build()
    }
}
