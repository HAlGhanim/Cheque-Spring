Feature: Account Management

  Scenario: Successfully create a new Account
    Given a valid account creation request
    When the account is submitted
    Then the account should be successfully created

  Scenario: Fail to create Account with invalid data
    Given an invalid account creation request
    When the account is submitted
    Then validation error message is shown for account