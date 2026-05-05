Feature: Interact with the buttons sandbox page

  Background:
    Given the user is on the buttons page

  @buttons @normal-cases
  Scenario Outline: The user click a button and the page show the respective message
    When the user click the "<button>"
    Then The "<message>" should be correct based on the "<button>" pressed

    Examples:
    | button          | message                       |
    | Double Click Me | You have done a double click  |
    | Right Click Me  | You have done a right click   |
    | Click Me        | You have done a dynamic click |

  @buttons @mixed-cases
  Scenario Outline: The user can click buttons and the page show the respectives messages
    When the user click the buttons "<numbers>"
    Then The messages should be correct based on the buttons "<numbers>" pressed

    Examples:
      | numbers  |
      | 1,2     |
      | 2,3     |
      | 1,2,3   |
      | 3,2,1   |
      | 1,3     |
