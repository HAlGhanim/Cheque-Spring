import com.cornerstone.cheque.model.*
import com.cornerstone.cheque.repo.AccountRepository
import com.cornerstone.cheque.repo.UserRepository
import com.cornerstone.cheque.service.AccountService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.math.BigDecimal
import java.util.*
import kotlin.test.*

class AccountServiceTest {

    private lateinit var accountRepository: AccountRepository
    private lateinit var userRepository: UserRepository
    private lateinit var service: AccountService

    private val user = User(
        id = 1L,
        email = "newuser7@gmail.com",
        password = "pass123",
        role = Role.USER
    )

    @BeforeEach
    fun setup() {
        accountRepository = mock(AccountRepository::class.java)
        userRepository = mock(UserRepository::class.java)
        service = AccountService(accountRepository, userRepository)
    }

    @Test
    fun `should create customer account`() {
        val request = AccountRequest(
            balance = BigDecimal("200.0"),
            spendingLimit = 1000,
            currency = "KWD",
            accountType = AccountType.CUSTOMER
        )

        `when`(userRepository.findById(1L)).thenReturn(Optional.of(user))
        `when`(accountRepository.findByUser(user)).thenReturn(null)
        `when`(accountRepository.save(any(Account::class.java))).thenAnswer { it.arguments[0] }

        val result = service.create(1L, request)

        assertEquals("KWD", result.currency)
        assertEquals(AccountType.CUSTOMER, result.accountType)
    }
}