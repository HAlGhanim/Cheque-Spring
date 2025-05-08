package com.cornerstone.cheque.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.cornerstone.cheque.model.KYC
import com.cornerstone.cheque.model.KYCRequest
import com.cornerstone.cheque.model.KYCResponse
import com.cornerstone.cheque.repo.KYCRepository
import com.cornerstone.cheque.repo.UserRepository
import com.cornerstone.cheque.service.KYCService

@RestController
@RequestMapping("/api/kyc")
class KYCController(private val service: KYCService,
                    private val repository: KYCRepository,
                    private val userRepository: UserRepository
) {
    @PostMapping
    fun create(@RequestBody request: KYCRequest): ResponseEntity<out Any?> {
        return try {
        val user = userRepository.findById(request.userId)
            .orElseThrow { RuntimeException("User not found") }

        val kyc = KYC(
            user = user,
            name = request.name,
            phone = request.phone
        )
        return ResponseEntity.ok(repository.save(kyc))

    } catch (ex: Exception) {
        ex.printStackTrace()
        ResponseEntity.status(500).body(mapOf("error" to "Internal server error", "details" to ex.message))
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
    @GetMapping("/user/{userId}")
    fun getByUserId(@PathVariable userId: Long): ResponseEntity<KYC> {
        val result = service.getByUserId(userId)
        return if (result != null) ResponseEntity.ok(result)
        else ResponseEntity.notFound().build()
    }
}
