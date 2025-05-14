package com.cornerstone.cheque.service

import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class CurrencyService {
    private val rates = mapOf(
        "KWD" to 1.0,
        "USD" to 0.31,
        "EUR" to 0.28,
        "GBP" to 0.24
    )
    fun convertToKWD(amount: BigDecimal, fromCurrency: String): BigDecimal {
        val rate = rates[fromCurrency]
            ?: throw IllegalArgumentException("Unsupported currency: $fromCurrency")
        return amount.multiply(BigDecimal.valueOf(rate))
    }

}