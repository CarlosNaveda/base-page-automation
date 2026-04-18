package model;

import valueObject.CheckboxLabel;
import java.util.Map;

public class ResolverCheckboxLabel {

    private static final Map<String, CheckboxLabel> MAP_LABEL = Map.ofEntries(
            Map.entry("Select Home", CheckboxLabel.HOME),
            Map.entry("Select Desktop", CheckboxLabel.DESKTOP),
            Map.entry("Select Notes", CheckboxLabel.NOTES),
            Map.entry("Select Commands", CheckboxLabel.COMMANDS),
            Map.entry("Select Documents", CheckboxLabel.DOCUMENTS),
            Map.entry("Select WorkSpace", CheckboxLabel.WORKSPACE),
            Map.entry("Select React", CheckboxLabel.REACT),
            Map.entry("Select Angular", CheckboxLabel.ANGULAR),
            Map.entry("Select Veu", CheckboxLabel.VEU),
            Map.entry("Select Office", CheckboxLabel.OFFICE),
            Map.entry("Select Public", CheckboxLabel.PUBLIC),
            Map.entry("Select Private", CheckboxLabel.PRIVATE),
            Map.entry("Select Classified", CheckboxLabel.CLASSIFIED),
            Map.entry("Select General", CheckboxLabel.GENERAL),
            Map.entry("Select Downloads", CheckboxLabel.DOWNLOADS),
            Map.entry("Select Word File.doc", CheckboxLabel.WORD_FILE),
            Map.entry("Select Excel File.doc", CheckboxLabel.EXCEL_FILE)
    );

    public static CheckboxLabel getMapLabel(String label){
        return MAP_LABEL.get(label);
    }

}

