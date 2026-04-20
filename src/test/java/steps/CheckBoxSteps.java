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

    @When("the {word} change to {string} state")
    public void theParentChangeToFinalState(String parent, String FinalState) {
        checkboxPage.setCheckboxState(parent, FinalState);
    }

    @Then("the {word} should be in {string} state")
    public void theParentIsOnTheFinalState(String parent, String FinalState) {
        checkboxPage.validationFinalState(parent, FinalState);

    }


}
