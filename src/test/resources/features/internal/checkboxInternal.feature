Feature: Internal test for methods

  Background:
    Given the user is on the check box page

  @internal
  Scenario Outline: Changing states for checkboxes
    Given the <parent> is "<initial_state>" state
    When the <parent> change to "<final_state>" state
    Then the <parent> should be in "<final_state>" state

    Examples:
     | parent           | initial_state  |  final_state    |
     | HOME             | SELECTED       |  NOT_SELECTED   |
     | DESKTOP          | INDETERMINATE  |  INDETERMINATE  |
     | DOCUMENTS        | SELECTED       |  NOT_SELECTED   |
     | WORKSPACE        | SELECTED       |  INDETERMINATE  |
     | OFFICE           | INDETERMINATE  |  SELECTED       |
     | DOWNLOADS        | INDETERMINATE  |  NOT_SELECTED   |

