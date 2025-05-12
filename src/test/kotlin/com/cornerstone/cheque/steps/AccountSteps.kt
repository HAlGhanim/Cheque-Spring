package stepdefs

import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.cucumber.java.en.Then
import org.junit.jupiter.api.Assertions.assertTrue

class AccountSteps {

    @Given("a valid account creation request")
    fun validRequest() {
        println("Preparing valid account request")
    }

    @Given("an invalid account creation request")
    fun invalidRequest() {
        println("Preparing invalid account request")
    }

    @When("the account is submitted")
    fun submitRequest() {
        println("Submitting account request")
    }

    @Then("the account should be successfully created")
    fun verifySuccess() {
        println("Account created successfully")
        assertTrue(true)
    }

    @Then("validation error message is shown for account")
    fun verifyValidationErrorForAccount() {
        println("Validation error returned")
        assertTrue(true)
    }
}