package com.cornerstone.cheque

import com.cornerstone.cheque.controller.UserController
import com.cornerstone.cheque.model.Role
import com.cornerstone.cheque.model.User
import com.cornerstone.cheque.repo.UserRepository
import com.cornerstone.cheque.service.UserService
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
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ChequeApplicationTests {

	private lateinit var userService: UserService
	private lateinit var passwordEncoder: PasswordEncoder
	private lateinit var controller: UserController

	@BeforeEach
	fun setup() {
		userService = mock(UserService::class.java)
		passwordEncoder = mock(PasswordEncoder::class.java)
		controller = UserController(userService, userRepository, passwordEncoder)
	}

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
	@Test
	fun `should get all users`() {
		val users = listOf(User(
			email = "newuser7@gmail.com",
			password = "pass123",
			role = Role.USER
		))
		`when`(userService.getAll()).thenReturn(users)

		val response = controller.getAll()

		assertEquals(HttpStatus.OK, response.statusCode)
		assertEquals(1, response.body?.size)
	}

	@Test
	fun `should get user by ID`() {
		val user = User(
			email = "newuser7@gmail.com",
			password = "pass123",
			role = Role.USER
		)
		`when`(userService.getById(1)).thenReturn(user)

		val response = controller.getById(1)

		assertEquals(HttpStatus.OK, response.statusCode)
		assertEquals("newuser7@gmail.com", response.body?.email)
	}

}
