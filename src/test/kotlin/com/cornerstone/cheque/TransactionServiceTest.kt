import com.cornerstone.cheque.model.*
import com.cornerstone.cheque.repo.TransactionRepository
import com.cornerstone.cheque.repo.AccountRepository
import com.cornerstone.cheque.service.TransactionService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.test.*
import org.mockito.Mockito.mock
import kotlin.jvm.java


class TransactionServiceTest {

    private lateinit var transactionRepository: TransactionRepository
    private lateinit var accountRepository: AccountRepository
    private lateinit var service: TransactionService

    private val user = User(
        email = "newuser7@gmail.com",
        password = "pass123",
        role = Role.USER
    )

    @BeforeEach
    fun setup() {
        transactionRepository = mock(TransactionRepository::class.java)
        accountRepository = mock(AccountRepository::class.java)
        val currencyService = mock(CurrencyService::class.java)
        service = TransactionService(transactionRepository, accountRepository, currencyService)
    }

    @Test
    fun `should create transaction`() {
        val account = Account(
            accountNumber = "7738384767373",
            user = user,
            balance = BigDecimal("1000.000"),
            spendingLimit = 500,
            currency = "KWD",
            accountType = AccountType.CUSTOMER,
            createdAt = LocalDateTime.now()
        )
        val transaction = Transaction(
            senderAccount = account,
            receiverAccount = account,
            amount = BigDecimal("150.000"),
            createdAt = LocalDateTime.now()
        )

        `when`(transactionRepository.save(any(Transaction::class.java))).thenReturn(transaction)

        val result = service.create(transaction)

        assertEquals(BigDecimal("150.000"), result.amount)
        assertEquals("7738384767373", result.senderAccount.accountNumber)
    }
}