package pages.elements;

import model.CheckboxNode;
import pages.BasePage;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CheckboxPage extends BasePage {

    public CheckboxPage() {
        super();
    }
    CheckboxNode checkboxNode;




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

    private String getXpathCheckbox(String parentName){
        return CHECKBOXES.get(parentName);
    }

    //ESTO HAY QUE ARMARLO DINÁMICAMENTE
    private void createTreeCheckbox() {
    }


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


    //Métodos privados


    //EN TEST


    private int generateRandomNumber (int max) {
        return (int) (Math.random() * max) + 1;
    }

    private String joinActualExpectedState(String actualState, String expectedState){
        return actualState +"-"+expectedState;
    }




    //Métodos públicos


    //POR REPLANTEARLO
    public void setParentCheckboxState(String parent, String expectedState) {



    }





}