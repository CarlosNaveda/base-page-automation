Feature: Interact with the web tables sandbox page

  Background:
    Given the user is on the web tables page

  @webTable @add
  Scenario: The user add a information to registers a employee
    Given the user click the add button
    When the user fill all the information requested by the form with:
      | firstName  |  lastName | age | email                 | salary   | department    |
      | Carlos     |  Naveda    | 39  | cnaveda@example.com  | 10000    | QA Automation |

    And the user click the button submit
    Then the all the information registered should be show in the web table

  @webTable @search
  Scenario Outline: The user search word and the results show in the web table
    Given the user type a <word> in the search box
    When the user click the button search
    Then the coincidence with the <word> show in the web table

    Examples:
      | word     |
      | Cierra   |
      | @        |
      | Al       |
      | rra      |
      | Cantrell |
      | Kierra   |
      | 2000     |
      | example  |
      | 45       |
      | Compli   |


#  @webTable @pagination
#  @webTable @combo-show
#  @webTable @edit
#  @webTable @delete