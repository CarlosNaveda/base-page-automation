package steps;

import io.cucumber.java.en.Given;
import pages.elements.CheckboxPage;

public class CheckBoxSteps {

    CheckboxPage checkboxPage = new CheckboxPage();

    @Given("the {word} is {string} state")
    public void theParentIsInitialState(String parent, String state){
        checkboxPage.setParentCheckboxState(parent, state);
    }


}
