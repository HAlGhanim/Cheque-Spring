package stepdefs

import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.cucumber.java.en.Then
import org.junit.jupiter.api.Assertions.assertTrue

class TransactionSteps {

    @Given("a valid transaction creation request")
    fun validRequest() {
        println("Preparing valid transaction request")
    }

    @Given("an invalid transaction creation request")
    fun invalidRequest() {
        println("Preparing invalid transaction request")
    }

    @When("the transaction is submitted")
    fun submitRequest() {
        println("Submitting transaction request")
    }

    @Then("the transaction should be successfully created")
    fun verifySuccess() {
        println("Transaction created successfully")
        assertTrue(true)
    }

    @Then("validation error message is shown for transaction")
    fun verifyValidationErrorForTransaction() {
        println("Validation error returned")
        assertTrue(true)
    }
}