Feature: Interact with the textbox sandbox page

  Background:
    Given the user is on the text box page

  @textbox @positive
  Scenario: The user can fill section Text Box and click the button Submit
    And fill textboxes on the page
    And click the button Submit
    Then can confirm his information in the output section

  @textbox @negative
  Scenario: The user don't fill email textbox click the button Submit
    And fill partial the email textbox on the page
    And click the button Submit
    Then can confirm the email validation message