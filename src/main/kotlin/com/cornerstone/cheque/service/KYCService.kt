package com.cornerstone.cheque.service

import com.cornerstone.cheque.model.KYC
import com.cornerstone.cheque.repo.KYCRepository
import org.springframework.stereotype.Service

@Service
class KYCService(private val repository: KYCRepository) {

    fun create(entity: KYC): KYC = repository.save(entity)

    fun getById(id: Long): KYC? = repository.findById(id).orElse(null)

    fun getAll(): List<KYC> = repository.findAll()
}
