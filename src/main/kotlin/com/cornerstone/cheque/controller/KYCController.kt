package com.cornerstone.cheque.controller

import com.cornerstone.cheque.model.KYC
import com.cornerstone.cheque.model.KycRequest
import com.cornerstone.cheque.model.User
import com.cornerstone.cheque.repo.KYCRepository
import com.cornerstone.cheque.repo.UserRepository
import com.cornerstone.cheque.service.KYCService
import com.cornerstone.cheque.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/kycs")
class KYCController(private val service: KYCService,
                    private val repository: KYCRepository,
                    private val userRepository: UserRepository
) {

    @PostMapping
    fun create(@RequestBody request: KycRequest): ResponseEntity<KYC> {

        val user = userRepository.findById(request.userId)
            .orElseThrow { RuntimeException("User not found") }

        val kyc = KYC(
            user = user,
            name = request.name,
            phone = request.phone
        )

        return ResponseEntity.ok(repository.save(kyc))
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
}
