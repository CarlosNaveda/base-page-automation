Feature: Internal test for methods

  Background:
    Given the user is on the check box page

  @internal
  Scenario Outline: Changing states for checkboxes
    Given the <parent> is "<initial_state>" state
#    When the <parent> change to "<final_state>" state

    Examples:
     | parent           | initial_state  |  final_state    |
     | homeCheckbox     | NOT_SELECTED   |  SELECTED       |
#     | desktopCheckbox  | NOT_SELECTED   |  INDETERMINATE  |
#     | documentsCheckbox| SELECTED       |  NOT_SELECTED   |
#     | workspaceCheckbox| SELECTED       |  INDETERMINATE  |
#     | officeCheckbox   | NOT_SELECTED   |  INDETERMINATE  |
#     | downloadsCheckbox| SELECTED       |  NOT_SELECTED  |

