Feature: Interact with the checkbox sandbox page

  Background:
    Given the user is on the radio button page

  @radioButtons @positive
  Scenario Outline: Selecting the radio button the text success change
    When the radiobutton <radio_button_name> is selected
    Then the text success show the <text_output>

    Examples:
      | radio_button_name       |  text_output  |
      |  Yes                    |  Yes          |
      |  Impressive             |  Impressive   |


  @radioButtons @negative
  Scenario Outline: Selecting the radio button No the text success doesn't change
    When the radiobutton <radio_button_name> is selected
    Then the text success doesn't show

    Examples:
      | radio_button_name |
      |  No               |
