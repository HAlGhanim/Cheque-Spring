package com.cornerstone.cheque.controller

import com.cornerstone.cheque.model.KYC
import com.cornerstone.cheque.model.KYCRequest
import com.cornerstone.cheque.repo.KYCRepository
import com.cornerstone.cheque.repo.UserRepository
import com.cornerstone.cheque.service.KYCService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/kyc")
class KYCController(
    private val service: KYCService,
    private val repository: KYCRepository,
    private val userRepository: UserRepository
) {

    @PostMapping
    fun create(@RequestBody request: KYCRequest, principal: Principal): ResponseEntity<KYC> {
        return try {
            val userId = principal.name.toLong()
            val user = userRepository.findById(userId).orElseThrow { RuntimeException("User not found") }

            val kyc = KYC(
                user = user,
                name = request.name,
                phone = request.phone
            )
            ResponseEntity.ok(repository.save(kyc))

        } catch (ex: Exception) {
            ex.printStackTrace()
            ResponseEntity.status(500).build()
        }
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<KYC>> =
        ResponseEntity.ok(service.getAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<KYC> {
        val result = service.getById(id)
        return if (result != null) ResponseEntity.ok(result)
        else ResponseEntity.notFound().build()
    }

    @GetMapping("/user")
    fun getByUserId(principal: Principal): ResponseEntity<KYC> {
        val userId = principal.name.toLong()
        val result = service.getByUserId(userId)
        return if (result != null) ResponseEntity.ok(result)
        else ResponseEntity.notFound().build()
    }
}
