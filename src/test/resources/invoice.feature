Feature: Invoice Management

  Scenario: Successfully create a new Invoice
    Given a valid invoice creation request
    When the invoice is submitted
    Then the invoice should be successfully created

  Scenario: Fail to create Invoice with invalid data
    Given an invalid invoice creation request
    When the invoice is submitted
    Then a validation error should be returned
