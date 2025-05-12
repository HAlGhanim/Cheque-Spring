Feature: PaymentLink Management

  Scenario: Successfully create a new PaymentLink
    Given a valid paymentlink creation request
    When the paymentlink is submitted
    Then the paymentlink should be successfully created

  Scenario: Fail to create PaymentLink with invalid data
    Given an invalid paymentlink creation request
    When the paymentlink is submitted
    Then validation error should be shown for payment link