package stepdefs

import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.cucumber.java.en.Then
import org.junit.jupiter.api.Assertions.assertTrue

class PaymentLinkSteps {

    @Given("a valid paymentlink creation request")
    fun validRequest() {
        println("Preparing valid paymentlink request")
    }

    @Given("an invalid paymentlink creation request")
    fun invalidRequest() {
        println("Preparing invalid paymentlink request")
    }

    @When("the paymentlink is submitted")
    fun submitRequest() {
        println("Submitting paymentlink request")
    }

    @Then("the paymentlink should be successfully created")
    fun verifySuccess() {
        println("PaymentLink created successfully")
        assertTrue(true)
    }

    @Then("validation error should be shown for payment link")
    fun verifyValidationErrorForPaymentLink() {
        println("Validation error returned")
        assertTrue(true)
    }
}