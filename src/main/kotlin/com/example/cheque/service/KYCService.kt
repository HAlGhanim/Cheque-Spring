package com.example.cheque.service

import com.example.cheque.model.KYC
import com.example.cheque.repository.KYCRepository
import org.springframework.stereotype.Service

@Service
class KYCService(private val repository: KYCRepository) {

    fun create(entity: KYC): KYC = repository.save(entity)

    fun getById(id: Long): KYC? = repository.findById(id).orElse(null)

    fun getAll(): List<KYC> = repository.findAll()
}
