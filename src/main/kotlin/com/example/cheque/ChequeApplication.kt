package com.example.cheque

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChequeApplication

fun main(args: Array<String>) {
	runApplication<ChequeApplication>(*args)
}
