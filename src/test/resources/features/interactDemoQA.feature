Feature: Interact with the sandbox page https://demoqa.com/

  Scenario: The user can fill section Text Box and click the button Submit
    Given the user go to the page DemoQa
    When click on the elements section
    And click on the text box accordion option
    And fill textboxes on the page
    And click the button Submit
    Then can confirm his information in the output section