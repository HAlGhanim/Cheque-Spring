package com.cornerstone.cheque.service

import com.cornerstone.cheque.model.KYC
import com.cornerstone.cheque.model.User
import com.cornerstone.cheque.repo.KYCRepository
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.stereotype.Service

@Service
class KYCService(private val repository: KYCRepository) {

    fun create(kyc: KYC): KYC = repository.save(kyc)

    fun getAll(): List<KYC> = repository.findAll()
    fun getById (id: Long): KYC = repository.findById(id).orElse(null)

}
