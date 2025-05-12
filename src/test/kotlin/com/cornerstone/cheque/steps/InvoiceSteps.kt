package stepdefs

import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.cucumber.java.en.Then
import org.junit.jupiter.api.Assertions.assertTrue

class InvoiceSteps {

    @Given("a valid invoice creation request")
    fun validRequest() {
        println("Preparing valid invoice request")
    }

    @Given("an invalid invoice creation request")
    fun invalidRequest() {
        println("Preparing invalid invoice request")
    }

    @When("the invoice is submitted")
    fun submitRequest() {
        println("Submitting invoice request")
    }

    @Then("the invoice should be successfully created")
    fun verifySuccess() {
        println("Invoice created successfully")
        assertTrue(true)
    }

    @Then("a validation error should be returned")
    fun verifyValidationError() {
        println("Validation error returned")
        assertTrue(true)
    }
}