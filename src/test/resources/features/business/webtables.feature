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

  @webTable @pagination
  Scenario Outline: The user do pagination in web tables and the pagination is updated
    Given The user add <records> of information to registers a employee
    When the user change to "<control_pagination>"
    Then The text pagination updates correctly by "<control_pagination>"

    Examples:
      |records| control_pagination |
      | 30    | Show 10            |
      | 25    | Show 20            |
      | 50    | Show 30            |
      | 55    | Show 40            |
      | 60    | Show 50            |


#  @webTable @combo-show



#  @webTable @edit
#  @webTable @delete