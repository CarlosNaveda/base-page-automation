Feature: Interact with the broken links sandbox page

  Background:
    Given The user is on the broken links page

  @brokenLinks @image
  Scenario Outline: The user see the item and obtain the item's description
    When the user see the "<item>"
    Then can obtain the correct "<description>"

    Examples:
      | item           | description  |
      | Valid image    | Valid image  |
      | Invalid image  | Broken image |
      | Valid link     | Valid Link   |
      | Broken link    | Broken Link  |

  @brokenLinks @link
  Scenario Outline: The user click the item and check url of the changed page
    When the user click the "<item>"
    Then can obtain the "<url>" of the changed page

    Examples:
      | item           | url                                                  |
      | Valid link     | https://demoqa.com/                                  |
      | Broken link    | https://the-internet.herokuapp.com/status_codes/500  |