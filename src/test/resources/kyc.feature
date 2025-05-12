Feature: KYC Management

  Scenario: Successfully create a new KYC
    Given a valid kyc creation request
    When the kyc is submitted
    Then the kyc should be successfully created

  Scenario: Fail to create KYC with invalid data
    Given an invalid kyc creation request
    When the kyc is submitted
    Then validation error should be shown for KYC