package com.cornerstone.cheque.service

import com.cornerstone.cheque.model.RedeemCode
import com.cornerstone.cheque.repo.AccountRepository
import com.cornerstone.cheque.repo.RedeemCodeRepository
import com.cornerstone.cheque.repo.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class RedeemService(
    private val redeemCodeRepository: RedeemCodeRepository,
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository
) {

    fun generate(amount: BigDecimal): RedeemCode {
        val code = UUID.randomUUID().toString().substring(0, 8).uppercase()
        val redeemCode = RedeemCode(
            code = code,
            amount = amount,
            used = false
        )
        return redeemCodeRepository.save(redeemCode)
    }

    @Transactional
    fun redeem(code: String, userEmail: String): String {

        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("User not found")

        val account = accountRepository.findByUser(user)
            ?: throw IllegalArgumentException("Account not found")

        val redeemCode = redeemCodeRepository.findByCode(code)
            ?: throw IllegalArgumentException("Invalid code")


        if (redeemCode.used) {
            throw IllegalStateException("Code already used")
        }

        // Update account balance
        account.balance += redeemCode.amount
        redeemCode.used = true

        accountRepository.save(account)
        redeemCodeRepository.save(redeemCode)

        return "Balance updated by ${redeemCode.amount}. New balance: ${account.balance}"
    }
    fun countActiveCodes(): Long {
        return redeemCodeRepository.countByUsedFalse()
    }
}