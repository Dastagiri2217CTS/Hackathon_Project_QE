Feature: Finding Hospitals on Practo website

  Background:
    Given the user is on the Practo website home page


  @Smoke @Search
  Scenario: Find Hospitals in Bangalore open 24x7 with parking and rating above 3.5
    When the user searches for "Bangalore" location and "Hospital" service
    Then the hospitals with parking and rating above 3.5 are displayed

  @Smoke @Navigation
  Scenario: Capture top cities from Diagnostics page on Practo website
    When the user clicks on Lab Tests
    Then the top diagnostic cities are displayed

  @Smoke @FormValidation
  Scenario: Verify submit button is disabled when Corporate Wellness form has invalid phone number
    When the user navigates to Corporate Wellness page
    And the user fills the form with name "Gopal" organization "Cognizant" phone "8970657" email "gopal@cognizant.com"
    Then the submit button should be disabled