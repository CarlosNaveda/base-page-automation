Feature: Interact with the upload download sandbox page

  Background:
    Given the user is on the uploadDownload page

  @upload
  Scenario Outline: The user select a file and the path shows correct
    When the user click the select file button
    And select "<file>"
    Then The path shows the fake path with name of "<file>"

    Examples:
      | file                                       |
      | C:\Users\Carlos\Downloads\estrella.png     |
      | C:\Users\Carlos\Downloads\blog.png         |


  @download
  Scenario Outline: The user click the download button and the sampleFile is downloaded
    When the user click the download button
    Then the "<file>" is downloaded

    Examples:
      | file            |
      | sampleFile.jpeg |

