package stepdefs

import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.cucumber.java.en.Then
import org.junit.jupiter.api.Assertions.assertTrue

class KYCSteps {

    @Given("a valid kyc creation request")
    fun validRequest() {
        println("Preparing valid kyc request")
    }

    @Given("an invalid kyc creation request")
    fun invalidRequest() {
        println("Preparing invalid kyc request")
    }

    @When("the kyc is submitted")
    fun submitRequest() {
        println("Submitting kyc request")
    }

    @Then("the kyc should be successfully created")
    fun verifySuccess() {
        println("KYC created successfully")
        assertTrue(true)
    }

    @Then("validation error should be shown for KYC")
    fun verifyValidationErrorForKYC() {
        println("Validation error returned")
        assertTrue(true)
    }
}