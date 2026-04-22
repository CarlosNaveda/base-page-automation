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
    public void selectionContextIsInitialState(String Context, String parent, String state) {
        checkboxPage.selectionContextIsInitialState(Context, parent,state);
    }

    @Given("the children of {word} is {string} state")
    public void theChildrenIsInitialState(String parent, String state) {
        checkboxPage.setCheckboxesState(parent,state);
    }

    @When("the {word} change to {string} state")
    public void theParentChangeToFinalState(String parent, String finalState) {
        checkboxPage.setCheckboxState(parent, finalState);
    }

    @When("the user {word} the {word}")
    public void theUserActionTheElement(String action,String element) {
        checkboxPage.theUserActionTheElement(action,element);
    }

    @When("the user {word} {string} of {word}")
    public void theUserActionContext(String action, String contextOfSelection,String parent) {
        checkboxPage.theUserActionContext(action, contextOfSelection, parent);
    }

    @When("the user {word} the children of {word}")
    public void theUserActionOnChildren(String action,String parent) {
        checkboxPage.theUserActionOnChildren(action,parent);
    }

    @Then("the {word} should be in {string} state")
    public void theElementIsOnTheFinalState(String element, String finalState) {
        checkboxPage.validationFinalState(element, finalState);
    }

    @Then("all the children of {word} should be in {string} state")
    public void allTheChildrenOfParentIsOnFinalState(String parent, String finalState) {
        checkboxPage.validationChildrenFinalState(parent,finalState);
    }

    @Then("the {word} {string} in the text output")
    public void theElementShouldBeOnTheTextOutput(String element, String expectedBehavior) {
        checkboxPage.validationElementOnTheTextOutput(element,expectedBehavior);
    }

    @Then("the children and {word} {string} in the text output")
    public void theChildrenAndParentShouldBeOnTheTextOutput(String parent, String expectedBehavior) {
        checkboxPage.validationParentChildrenOnTheTextOutput(parent, expectedBehavior);
    }



}
