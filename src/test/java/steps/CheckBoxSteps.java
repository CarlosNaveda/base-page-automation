package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.elements.CheckboxPage;

public class CheckBoxSteps {

    CheckboxPage checkboxPage = new CheckboxPage();

    @Given("the {word} is {string} state")
    public void theParentIsInitialState(String parent, String initialState) {
        checkboxPage.setCheckboxState(parent, initialState);
    }

    @Given("the {string} of {word} is {string} state")
    public void selectionOfChildrenIsInitialState(String initialContext, String parent, String state) {
        checkboxPage.selectionOfChildrenIsInitialState(initialContext, parent,state);
    }


    @When("the {word} change to {string} state")
    public void theParentChangeToFinalState(String parent, String FinalState) {
        checkboxPage.setCheckboxState(parent, FinalState);
    }

    @When("the user select the {word}")
    public void theUserSelectTheSon(String son) {
        checkboxPage.theUserSelectTheSon(son);
    }

    @When("the user select {string} of {word}")
    public void theUserSelectContext(String contextOfSelection,String parent) {
        checkboxPage.theUserSelectContext(contextOfSelection, parent);
    }

    @When("the user deselect {string} of {word}")
    public void theUserDeselectContext(String contextOfSelection,String parent) {
        checkboxPage.theUserDeselectContext(contextOfSelection,parent);
    }

    @Then("the {word} should be in {string} state")
    public void theParentIsOnTheFinalState(String parent, String FinalState) {
        checkboxPage.validationFinalState(parent, FinalState);

    }


}
