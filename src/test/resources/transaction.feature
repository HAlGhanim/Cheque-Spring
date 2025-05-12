Feature: Transaction Management

  Scenario: Successfully create a new Transaction
    Given a valid transaction creation request
    When the transaction is submitted
    Then the transaction should be successfully created

  Scenario: Fail to create Transaction with invalid data
    Given an invalid transaction creation request
    When the transaction is submitted
    Then validation error message is shown for transaction