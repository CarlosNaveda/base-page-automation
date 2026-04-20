Feature: Interact with the checkbox sandbox page

  Background:
    Given the user is on the check box page

  @son-parent
  Scenario Outline: Selecting a child checkbox updates the parent state
    Given the "<initial_context>" of <son's_parent> is "<son_initial_state>" state
    When the user select "<context_of_selection>" of <son's_parent>
    Then the <son's_parent> should be in "<parent_final_state>" state

    Examples:
     | initial_context       |  son_initial_state   | context_of_selection       |  son's_parent  | parent_final_state |
     |  all children         |  NOT_SELECTED        | a single child             |  DESKTOP       |  INDETERMINATE     |
     |  all children         |  NOT_SELECTED        | all children               |  OFFICE        |  SELECTED          |
     |  all children         |  NOT_SELECTED        | the last child             |  WORKSPACE     |  INDETERMINATE     |



  @son-parent
  Scenario Outline: Deselecting a child checkbox updates the parent state
    Given the "<initial_context>" of <son's_parent> is "<son_initial_state>" state
    When the user deselect "<context_of_deselection>" of <son's_parent>
    Then the <son's_parent> should be in "<parent_final_state>" state

    Examples:
      | initial_context       |  son_initial_state   | context_of_deselection     |  son's_parent  | parent_final_state |
      |  all children         |  SELECTED            | a single child             |  DOCUMENTS     |  INDETERMINATE     |
      |  all children         |  SELECTED            | all children               |  HOME          |  NOT_SELECTED      |
      |  all children         |  SELECTED            | the last child             |  DOWNLOADS     |  INDETERMINATE     |