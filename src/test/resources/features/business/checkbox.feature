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





  @parent-son
  Scenario Outline: Selecting a parent checkbox updates the children states
    Given the <parent> is "<parent_initial_state>" state
    When the user <action> the <parent>
    Then all the children of <parent> should be in "<children_final_state>" state

    Examples:
   | parent   | parent_initial_state  | action         | children_final_state   |
   | HOME     |  NOT_SELECTED         | SELECTED       | SELECTED               |
   | HOME     |  SELECTED             | NOT_SELECTED   | NOT_SELECTED           |



#
#  @textoutputs
#  Scenario_4: Comportamiento de textos outputs
#  Nota: - La visualización del texto en el output corresponde a una secuencia en cascada desde la selección realizada es decir:
#  Teniendo el siguiente ejemplo:
#  Home
#  |-----Desktop
#  |-----Notes
#  |-----Commands
#
#  Si seleccionamos el checkbox de "Commands" 	-->	output = "You have selected : commands"
#  Si luego seleccionamos el checkbox de "Notes 	-->	output = "You have selected : commands notes desktop"
#
#  Example_1: Seleccionar un hijo (Hay más hijos)				            |	En el output se muestra: "You have selected : " + la selección del hijo
#  Example_2: Seleccionar todos hijos					                    |	En el output se muestra: "You have selected : " + la selección de padre e hijos
#  Example_3: Selecciona el último hijo					                    |	En el output se muestra: "You have selected : " + la selección de padre e hijos
#  Example_4: Quitar selección de un hijo	(Hay más hijos seleccionados)	|	En el output desaparece la selección de hijo y del padre, pero debe mostrarse "You have selected : " + selección de otros hijos en caso estén seleccionados
#  Example_5: Quitar selección de todos hijos				                |	En el output desaparece la selección de hijo y del padre, tampoco debe mostrarse "You have selected : "
#  Example_6: Quitar selección del último hijo				                |	En el output desaparece la selección de hijo y del padre, tampoco debe mostrarse "You have selected : "
#  Example_7: Seleccionar Padre (Sin padre)				                    |	En el output se muestra: "You have selected : " + la selección de padre e hijo
#  Example_8: Quitar selección de Padre (Sin padre)			                |	En el output desaparece la selección de hijo y del padre, tampoco debe mostrarse "You have selected : "
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
