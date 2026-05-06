Feature: Interact with the links sandbox page

  Background:
    Given the user is on the links page

  @links @new-tab
  Scenario Outline: The user click a link and show a new page
    When the user click the new tab link "<link>"
    Then The new page have the "<url>" correct

    Examples:
      | link           | url                |
      | Simple Link    | https://demoqa.com/ |
      | Dynamic Link   | https://demoqa.com/ |

  @links @response
  Scenario Outline: The user click a link and show a number and text response
    When the user click the api call link "<link>"
    Then The page show the <number_response> and "<text_response>"

    Examples:
      | link              | number_response | text_response     |
      | Created Link      | 201             | Created           |
      | No Content Link   | 204             | No Content        |
      | Moved Link        | 301             | Moved Permanently |
      | Bad Request Link  | 400             | Bad Request       |
      | Unauthorized Link | 401             | Unauthorized      |
      | Forbidden Link    | 403             | Forbidden         |
      | Not Found Link    | 404             | Not Found         |




