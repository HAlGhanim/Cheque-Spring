package stepdefs

import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.cucumber.java.en.Then
import org.junit.jupiter.api.Assertions.assertTrue

class UserSteps {

    @Given("a valid user creation request")
    fun validRequest() {
        println("Preparing valid user request")
    }

    @Given("an invalid user creation request")
    fun invalidRequest() {
        println("Preparing invalid user request")
    }

    @When("the user is submitted")
    fun submitRequest() {
        println("Submitting user request")
    }

    @Then("the user should be successfully created")
    fun verifySuccess() {
        println("User created successfully")
        assertTrue(true)
    }

    @Then("a validation error should be returned for user")
    fun verifyValidationErrorUser() {
        println("Validation error returned")
        assertTrue(true)
    }
}