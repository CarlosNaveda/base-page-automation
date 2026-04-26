Feature: Interact with the checkbox sandbox page

  Background:
    Given the user is on the check box page

  @son-parent
  Scenario Outline: Selecting/Deselecting a child checkbox updates the parent state
    Given the "<initial_context>" of <children of> is "<son_initial_state>" state
    When the user <action> "<context_of_selection>" of <children of>
    Then the <children of> should be in "<parent_final_state>" state

    Examples:
     | initial_context       |  son_initial_state  | action       | context_of_selection  |  children of  | parent_final_state |
     |  all children         |  NOT_SELECTED       | SELECTED     | a single child        |  DESKTOP       |  INDETERMINATE     |
     |  all children         |  NOT_SELECTED       | SELECTED     | all children          |  OFFICE        |  SELECTED          |
     |  all children         |  NOT_SELECTED       | SELECTED     | the last child        |  WORKSPACE     |  INDETERMINATE     |
     |  all children         |  SELECTED           | NOT_SELECTED | a single child        |  DOCUMENTS     |  INDETERMINATE     |
     |  all children         |  SELECTED           | NOT_SELECTED | all children          |  HOME          |  NOT_SELECTED      |
     |  all children         |  SELECTED           | NOT_SELECTED | the last child        |  DOWNLOADS     |  INDETERMINATE     |


  @parent-son
  Scenario Outline: Selecting/Deselecting a parent checkbox updates the children states
    Given the <parent> is "<parent_initial_state>" state
    When the user <action> the <parent>
    Then all the children of <parent> should be in "<children_final_state>" state

    Examples:
   | parent   | parent_initial_state  | action         | children_final_state   |
   | HOME     |  NOT_SELECTED         | SELECTED       | SELECTED               |
   | HOME     |  SELECTED             | NOT_SELECTED   | NOT_SELECTED           |


  @textOutputs
  Scenario Outline: Selecting/Deselecting checkbox the text outputs show/don't this checkbox
    Given the <element> is "<element_initial_state>" state
    When the user <action> the <element>
    Then the <element> "<expected_behavior>" in the text output

    Examples:
      | element     | element_initial_state   | action        | expected_behavior     |
      | HOME        |  NOT_SELECTED           | SELECTED      | should be             |
      | WORD_FILE   |  NOT_SELECTED           | SELECTED      | should be             |
      | EXCEL_FILE  |  NOT_SELECTED           | SELECTED      | should be             |
      | ANGULAR     |  NOT_SELECTED           | SELECTED      | should be             |
      | COMMANDS    |  NOT_SELECTED           | SELECTED      | should be             |
      | REACT       |  SELECTED               | NOT_SELECTED  | should not be         |
      | GENERAL     |  SELECTED               | NOT_SELECTED  | should not be         |
      | DOCUMENTS   |  SELECTED               | NOT_SELECTED  | should not be         |
      | DESKTOP     |  SELECTED               | NOT_SELECTED  | should not be         |
      | CLASSIFIED  |  SELECTED               | NOT_SELECTED  | should not be         |

  @textOutputs
  Scenario Outline: Selecting/Deselecting all children checkboxes the text outputs show/don't children and parent
    Given the children of <parent> is "<children_initial_state>" state
    When the user <action> the children of <parent>
    Then the children and <parent> "<expected_behavior>" in the text output

    Examples:
      | parent      | children_initial_state   | action        | expected_behavior     |
      | HOME        | SELECTED                 | NOT_SELECTED  | should not be         |
      | WORKSPACE   | SELECTED                 | NOT_SELECTED  | should not be         |
      | OFFICE      | NOT_SELECTED             | SELECTED      | should be             |

  @textOutputs
  Scenario Outline: Selecting the last child checkbox the text outputs show children and parent
    Given the children of <parent> is all selected except one
    When the user select the not selected child of <parent>
    Then the children and <parent> "<expected_behavior>" in the text output

    Examples:
      | parent      | expected_behavior     |
      | DESKTOP     | should be             |
      | WORKSPACE   | should be             |
      | OFFICE      | should be             |
      | DOWNLOADS   | should be             |
      | DOCUMENTS   | should be             |
      | HOME        | should be             |

  @textOutputs
  Scenario Outline: Deselecting the last child checkbox the text outputs don't show the last child and parent
    Given the children of <parent> is all selected
    When the user deselect one of the child of <parent>
    Then the child and <parent> "<expected_behavior>" in the text output

    Examples:
      | parent      | expected_behavior         |
      | DESKTOP     | should not be             |
      | WORKSPACE   | should not be             |
      | OFFICE      | should not be             |
      | DOWNLOADS   | should not be             |
      | DOCUMENTS   | should not be             |
      | HOME        | should not be             |


#
#  @edgecases
#  Scenario_5: Comportamientos Límites
#
#  Example_1: Seleccionar todos los hijos y luego seleccionar el padre				            |	Se espera que el padre quede deseleccionado, por ende también sus hijos.
#  Example_2: Seleccionar Padre y luego seleccionar hijo						                |	Se espera que el hijo quede deseleccionado.
#  Example_3: Al tener Padre en estado indeterminado y luego seleccionarlo				        | 	Se espera que el padre quede seleccionado, por ende también todos sus hijos.
#  Example_4: Seleccionar checkbox, luego quitamos selección y luego volvemos a seleccionarlo 	|	Se espera que tanto el estado del checkbox y output sigan su comportamiento correcto.
#  Example_5: Seleccionar el checkbox "Word File.doc" 						                    |	Se espera que en el output se muestra "wordFile".
#  Example_6: Seleccionar el checkbox "Excel File.doc" 						                    |	Se espera que en el output se muestra "excelFile".
#  Example_7: Seleccionar checkboxes de distintas ramas						                    | 	Se espera que cada checkbox se visualice seleccionado y se visualicen en el output
#  Example_8: Seleccionar checkboxes de más arriba hacia abajo					                |	Se espera que el orden en el texto del output vaya desde la selección realizada hacia abajo y luego regrese desde arriba
#  Example_9: Seleccionar checkboxes de más abajo hacia arriba					                |	Se espera que el orden en el texto del output vaya desde la selección realizada hacia abajo y luego regrese desde arriba
#  Example_10: Seleccionar varias veces un checkbox						                        |	Se espera que el comportamiento de la selección y output siga las mismas reglas

#  @son-parent-spread
#  @parent-son-spread
