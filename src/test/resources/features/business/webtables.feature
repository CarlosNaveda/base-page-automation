Feature: Interact with the web tables sandbox page

  Background:
    Given the user is on the web tables page

  @registration-form
  Scenario: The user add a information to registers a employee
    Given the user fill all the information requested by the form
    When the user click the button submit
    Then the all the information registered should be show in the web table

