Feature: Interact with the checkbox sandbox page

  Background:
    Given the user goes to the page DemoQa
    When selects the elements section
    And selects the text box accordion option

  @son-parent
  Scenario Outline: Selecting a child checkbox updates the parent state
    Given the parent checkbox is <initial_state> state
    And the child checkboxes are multiples and <context>
    When I select <action> checkbox
    Then parent's checkbox change to <final_state> state

    Examples:
       | initial_state  | context                             | action           |  final_state    |
       | not selected   | none of them are selected           | a single child   |  indeterminate  |
       | not selected   | none of them are selected           | all children     |  selected       |
       | indeterminate  | all of them are selected except one | the last child   |  selected       |

  @son-parent
  Scenario Outline: Deselecting a child checkbox updates the parent state
    Given the parent checkbox is <initial_state> state
    And the child checkboxes are multiples and <context>
    When I deselect <action> checkbox
    Then parent's checkbox change to <final_state> state

    Examples:
      | initial_state  | context                                | action           |  final_state    |
      | selected       | all of them are selected               | a single child   |  indeterminate  |
      | selected       | all of them are selected               | all children     |  not selected   |
      | indeterminate  | all of them are selected except one    | the last child   |  not selected   |