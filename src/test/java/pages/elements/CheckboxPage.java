package pages.elements;

import model.CheckboxChildren;
import model.CheckboxParent;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

import java.util.*;

public class CheckboxPage extends BasePage {

    public CheckboxPage() {
        super();
    }

    CheckboxParent checkboxParent;
    CheckboxChildren checkboxChildren;


    //Variable de +
    private final String SWITCHER = "//span[contains(@class,'rc-tree-switcher_%s')]";


    //Variables Check box
    private final Map<String, String> CHECKBOXES = Map.ofEntries(
            Map.entry("homeCheckbox", "//span[@aria-label='Select Home']"),
            Map.entry("desktopCheckbox", "//span[@aria-label='Select Desktop']"),
            Map.entry("documentsCheckbox", "//span[@aria-label='Select Documents']"),
            Map.entry("workspaceCheckbox", "//span[@aria-label='Select WorkSpace']"),
            Map.entry("officeCheckbox", "//span[@aria-label='Select Office']"),
            Map.entry("downloadsCheckbox", "//span[@aria-label='Select Downloads']"),
            Map.entry("notesCheckbox", "//span[@aria-label='Select Notes']"),
            Map.entry("commandsCheckbox", "//span[@aria-label='Select Commands']"),
            Map.entry("wordCheckbox", "//span[@aria-label='Select Word File.doc']"),
            Map.entry("excelCheckbox", "//span[@aria-label='Select Excel File.doc']"),
            Map.entry("reactCheckbox", "//span[@aria-label='Select React']"),
            Map.entry("angularCheckbox", "//span[@aria-label='Select Angular']"),
            Map.entry("veuCheckbox", "//span[@aria-label='Select Veu']"),
            Map.entry("publicCheckbox", "//span[@aria-label='Select Public']"),
            Map.entry("privateCheckbox", "//span[@aria-label='Select Private']"),
            Map.entry("classifiedCheckbox", "//span[@aria-label='Select Classified']"),
            Map.entry("generalCheckbox", "//span[@aria-label='Select General']")
    );

    //Relaciones entre Checkboxes
    private final Map<String, List<String>> CHECKBOXES_PARENTS_RELATIONS = Map.of(
            "homeCheckbox", List.of("desktopCheckbox", "documentsCheckbox", "downloadsCheckbox"),
            "desktopCheckbox", List.of("notesCheckbox", "commandsCheckbox"),
            "documentsCheckbox", List.of("workspaceCheckbox", "officeCheckbox"),
            "workspaceCheckbox", List.of("reactCheckbox", "angularCheckbox", "veuCheckbox"),
            "officeCheckbox", List.of("publicCheckbox", "privateCheckbox", "classifiedCheckbox", "generalCheckbox"),
            "downloadsCheckbox", List.of("wordCheckbox", "excelCheckbox")
    );

    //Relaciones de estados feature vs web
    private final Map<String, String> CHECKBOXES_STATES = Map.of(
            "not selected", "false",
            "selected", "true",
            "indeterminate", "mixed"
    );


    //Estados de los checkboxes
    private final List<String> STATES = List.of("not selected", "selected", "indeterminate");


    //Métodos privados

    private void getStatesChildren (CheckboxParent checkboxParent) {

        System.out.println("Nombre del padre: " + checkboxParent.getParentName());
        //Nos aseguramos que el parent sea válido
        List<String> Children = CHECKBOXES_PARENTS_RELATIONS.get(checkboxParent.getParentName());

        //Recorro la lista de hijos
        for(String child : Children) {
            System.out.println("Nombre del Hijo actual: " + child);
            //Instancia el Hijo con su estado
            checkboxChildren = new CheckboxChildren(child,getStateOfCheckbox(getXpathCheckbox(child)));
            //Relaciono el Hijo con su padre
            checkboxParent.setChildren(checkboxChildren);

        }
    }

    private void StateNotSelectedToSelected (String child){
        click(child);
    }

    private String getXpathCheckbox(String parentName){
        return CHECKBOXES.get(parentName);
    }

    private void setStateFalseToTrue(CheckboxParent checkboxParent){
        click(getXpathCheckbox(checkboxParent.getParentName()));;
    }

    private void setStateFalseToMixed(){

    }

    private void setStateTrueToFalse(){

    }

    private void setStateTrueToMixed(){

    }

    private void setStateMixedToFalse(){

    }

    private void setStateMixedToTrue(){

    }


    //FALTA IMPLEMENTAR
    private void setTrueAtLeastOne (List<String> parentChildren, Map<String, String> childrenStatesMap){
        int trueCount = 0;

        for(String childrenState : childrenStatesMap.keySet()){
            //Si el estado del hijo en turno no es true
            if(childrenState.equals(CHECKBOXES_STATES.get("not selected")) || childrenState.equals(CHECKBOXES_STATES.get("indeterminate"))){
                //Lo pasamos a true

                trueCount++;
            }
        }

    }

    //FALTA IMPLEMENTAR
    //Es una lógica propia de ésta página
    private void parentCheckboxChangeState(CheckboxParent checkboxParent, String expectedState) {

        //Concatenamos el estado actual con lo esperado
        String transitionStates = checkboxParent.getParentState()+"-"+expectedState;

        System.out.println("Llega combinación de estado: " + transitionStates);

        switch (transitionStates) {
            case "false-true": // Checkbox No Seleccionado --> Checkbox Seleccionado
                //1 click al propio checkbox
                System.out.println("Entré false-true" + checkboxParent);
                setStateFalseToTrue(checkboxParent);
                break;

            case "false-mixed": // Checkbox No Seleccionado --> Checkbox Indeterminado
                //pasar a true al menos a alguno de sus checkbox hijos
                System.out.println("Entré false-mixed" + checkboxParent);
                setStateFalseToMixed();
                break;

            case "true-false": // Checkbox Seleccionado --> Checkbox No Seleccionado
                //1 click al propio checkbox
                System.out.println("Entré true-false" + checkboxParent);
                setStateTrueToFalse();
                break;

            case "true-mixed": // Checkbox Seleccionado --> Checkbox Indeterminado
                //pasar a false al menos a uno de sus checkbox hijos
                System.out.println("Entré true-mixed" + checkboxParent);
                setStateTrueToMixed();
                break;

            case "mixed-false": // Checkbox Indeterminado --> Checkbox No Seleccionado
                //pasar a false a todos sus checkbox hijos
                System.out.println("Entré mixed-false" + checkboxParent);
                setStateMixedToFalse();
                break;

            case "mixed-true": // Checkbox Indeterminado --> Checkbox Seleccionado
                //pasar a true a todos sus checkbox hijos
                System.out.println("Entré mixed-true" + checkboxParent);
                setStateMixedToTrue();
                break;


        }
    }

    //Métodos públicos

    //FALTA IMPLEMENTAR
    public void setParentCheckboxState(String parent, String initialState) {
        //Aquí solo ingresan parent checkboxes
        if (CHECKBOXES_PARENTS_RELATIONS.get(parent) != null) {
            //Abrimos todos los + de los checkbox padres
            openAllCloseSwitcher(SWITCHER);

            //Obtengo el estado actual del padre y el estado deseado
            System.out.println("Pasé lo switches");
            String actualState = getStateOfCheckbox(CHECKBOXES.get(parent));
            String expectedState = CHECKBOXES_STATES.get(initialState);

            //Si son diferentes, tengo que cambiar modificar el estado al deseado
           if(!Objects.equals(actualState, expectedState)){
               System.out.println("Entré porque el padre no tiene el estado deseado");
               //Creo la instancia del Checkbox Parent y Children
               checkboxParent = new CheckboxParent(parent,actualState);
               getStatesChildren(checkboxParent);
               parentCheckboxChangeState(checkboxParent,expectedState);
           }

        }
    }



}