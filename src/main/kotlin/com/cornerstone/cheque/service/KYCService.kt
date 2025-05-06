package com.cornerstone.cheque.service

import com.cornerstone.cheque.model.KYC
import com.cornerstone.cheque.model.User
import com.cornerstone.cheque.repo.KYCRepository
import com.cornerstone.cheque.repo.UserRepository
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.stereotype.Service

@Service
class KYCService(private val repository: KYCRepository, private val userRepository: UserRepository) {

    fun create(userId: Long, name: String, phone: String?) {
        val user = userRepository.findById(userId).orElseThrow()
        val kyc = KYC(
            user = user,
            name = name,
            phone = phone
        )
        repository.save(kyc)
    }

    fun getAll(): List<KYC> = repository.findAll()
    fun getById (id: Long): KYC = repository.findById(id).orElse(null)
}
data class Kyc(
    val id: Long,
    val name: String,
    val phone: String?
)