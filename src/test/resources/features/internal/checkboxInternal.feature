Feature: Internal test for methods

  Background:
    Given the user is on the check box page

  @internal
  Scenario Outline: Changing states for checkboxes
    Given the <parent> is "<initial_state>" state
    When the <parent> change to "<final_state>" state

    Examples:
     | parent           | initial_state  |  final_state    |
#     | homeCheckbox     | not selected   |  selected        |
#     | desktopCheckbox  | not selected   |  indeterminate  |
#     | documentsCheckbox| selected       |  not selected   |
     | workspaceCheckbox| selected       |  indeterminate  |
#     | officeCheckbox   | not selected   |  indeterminate  |
#     | downloadsCheckbox| selected       |  not selected   |

