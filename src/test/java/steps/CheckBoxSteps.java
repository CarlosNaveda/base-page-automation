package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import pages.elements.CheckboxPage;

public class CheckBoxSteps {

    CheckboxPage checkboxPage = new CheckboxPage();

    @Given("the {word} is {string} state")
    public void theParentIsInitialState(String parent, String initialState) {
        checkboxPage.setParentCheckboxState(parent, initialState);
    }

    @When("the {word} change to {string} state")
    public void theParentChangeToFinalState(String parent, String FinalState) {
        checkboxPage.setParentCheckboxState(parent, FinalState);
    }


}
