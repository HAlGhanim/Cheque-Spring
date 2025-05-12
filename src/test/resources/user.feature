Feature: User Management

  Scenario: Successfully create a new User
    Given a valid user creation request
    When the user is submitted
    Then the user should be successfully created

  Scenario: Fail to create User with invalid data
    Given an invalid user creation request
    When the user is submitted
    Then a validation error should be returned for user
