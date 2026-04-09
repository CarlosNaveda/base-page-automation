package pages.elements;

import pages.BasePage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckboxPage extends BasePage {

    public CheckboxPage() {
        super();
    }

    //Variables Check box
    private Map<String,String> checkboxes =  Map.ofEntries(
            Map.entry("homeCheckbox","//span[@aria-label='Select Home']"),
            Map.entry("desktopCheckbox","//span[@aria-label='Select Desktop']"),
            Map.entry("documentsCheckbox","//span[@aria-label='Select Documents']"),
            Map.entry("workspaceCheckbox","//span[@aria-label='Select WorkSpace']"),
            Map.entry("officeCheckbox","//span[@aria-label='Select Office']"),
            Map.entry("downloadsCheckbox","//span[@aria-label='Select Downloads']"),
            Map.entry("notesCheckbox","//span[@aria-label='Select Notes']"),
            Map.entry("commandsCheckbox","//span[@aria-label='Select Commands']"),
            Map.entry("wordCheckbox","//span[@aria-label='Select Word File.doc']"),
            Map.entry("excelCheckbox","//span[@aria-label='Select Excel File.doc']"),
            Map.entry("reactCheckbox","//span[@aria-label='Select React']"),
            Map.entry("angularCheckbox","//span[@aria-label='Select Angular']"),
            Map.entry("veuCheckbox","//span[@aria-label='Select Veu']"),
            Map.entry("publicCheckbox","//span[@aria-label='Select Public']"),
            Map.entry("privateCheckbox","//span[@aria-label='Select Private']"),
            Map.entry("classifiedCheckbox","//span[@aria-label='Select Classified']"),
            Map.entry("generalCheckbox","//span[@aria-label='Select General']")
    );

    //Relaciones entre Checkboxes
    private Map<String,List<String>> checkboxesRelations =  Map.of(
            "homeCheckbox",List.of("desktopCheckbox","documentsCheckbox","downloadsCheckbox"),
            "desktopCheckbox",List.of("notesCheckbox","commandsCheckbox"),
            "documentsCheckbox",List.of("workspaceCheckbox","officeCheckbox"),
            "workspaceCheckbox",List.of("reactCheckbox","angularCheckbox","veuCheckbox"),
            "officeCheckbox",List.of("publicCheckbox","privateCheckbox","classifiedCheckbox","generalCheckbox"),
            "downloadsCheckbox",List.of("wordCheckbox","excelCheckbox")
    );

    //Estados de los checkboxes
    private final List<String> STATES = List.of("not selected","selected","indeterminate");


    //Métodos privados


    //Métodos públicos
    public void setParentCheckboxState(String parent, String checkboxState){
        //Nos aseguramos que el parent sea válido
        List<String> parentChildren = checkboxesRelations.get(parent);
        if(parentChildren != null){
           //Hay que averiguar en qué estado se encuentra el parent (FALTA expandir el padre)
            String actualState=getStateOfCheckbox(checkboxes.get(parent));
            System.out.println("Estado actual: " + actualState);
        }


    }



}

