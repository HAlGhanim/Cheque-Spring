package com.cornerstone.cheque

import com.cornerstone.cheque.model.Role
import com.cornerstone.cheque.model.User
import com.cornerstone.cheque.repo.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.hamcrest.CoreMatchers.notNullValue
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ChequeApplicationTests {

	@Autowired
	lateinit var mockMvc: MockMvc

	@Autowired
	lateinit var userRepository: UserRepository

	private val objectMapper = jacksonObjectMapper()

	@Test
	fun `should register a new user`() {
		val newUser = User(
			email = "newuser7@gmail.com",
			password = "pass123",
			role = Role.USER
		)

		val result = mockMvc.post("/api/auth/register") {
			contentType = MediaType.APPLICATION_JSON
			content = objectMapper.writeValueAsString(newUser)
		}

		result.andExpect {
			status { isOk() }
		}

		val saved = userRepository.findByEmail("newuser7@gmail.com")
		assertNotNull(saved)
	}

	@Test
	fun `should login and recieve JWT token` () {
		val loginRequest = mapOf (
			"email" to "newuser7@gmail.com",
			"password" to "pass123"
		)

		val result = mockMvc.post("/api/auth/login") {
			contentType = MediaType.APPLICATION_JSON
			content = objectMapper.writeValueAsString(loginRequest)
		}
		result.andExpect {
			status {isOk()}
			jsonPath("$.token", notNullValue())
		}
	}


}
