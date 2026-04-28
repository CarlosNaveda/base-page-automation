package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.SoftAssertions;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import pages.elements.CheckboxPage;
import valueObject.CheckboxState;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Given("the children of {word} is all selected except one")
    public void theChildrenIsAllSelectedExceptOne(String parent) {
        checkboxPage.setChildrenAllSelectedExceptOne(parent);
    }

    @Given("the children of {word} is all selected")
    public void theChildrenIsAllSelected(String parent) {
        checkboxPage.setChildrenAllSelected(parent);
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

    @When("the user select the not selected child of {word}")
    public void theUserSelectTheLastChildNotSelected(String parent) {
        checkboxPage.theUserSelectTheLastChildNotSelected(parent);
    }

    @When("the user deselect one of the child of {word}")
    public void theUserDeselectOneOfTheChild(String parent) {
        checkboxPage.theUserDeselectOneOfChild(parent);
    }

    @Then("the {word} should be in {string} state")
    public void validateElementIsOnTheFinalState(String element, String finalState) {
        CheckboxState checkboxActualState = checkboxPage.getFinalState(element);
        CheckboxState checkboxExpectedState = CheckboxState.valueOf(finalState);
        Assert.assertEquals(checkboxActualState,checkboxExpectedState,"Los estados no coinciden");
    }

    @Then("all the children of {word} should be in {string} state")
    public void validateChildrenIsOnFinalState(String parent, String finalState) {
        List<CheckboxState> listActualState = checkboxPage.getChildrenStates(parent);
        CheckboxState ExpectedState = CheckboxState.valueOf(finalState);
        SoftAssert softAssert = new SoftAssert();

        for (CheckboxState ActualState : listActualState) {
            softAssert.assertEquals(ActualState,ExpectedState,"Los estados no coinciden");
        }

        softAssert.assertAll();
    }

    @Then("the {word} {string} in the text output")
    public void validateLabelOnTheTextOutput(String element, String expectedBehavior) {
        String labelExpected = checkboxPage.getLabelOfElement(element);
        List<String> listActualLabels = checkboxPage.getResultsLabels();

        if (expectedBehavior.equals("should be")) {
            assertThat(listActualLabels).as("El output debería contener el label seleccionado").contains(labelExpected);
        }
        else if (expectedBehavior.equals("should not be")) {
            assertThat(listActualLabels).as("El output no debería contener el label seleccionado").doesNotContain(labelExpected);
        }
    }

    @Then("the children and {word} {string} in the text output")
    public void validateChildrenAndParentOnTheTextOutput(String parent, String expectedBehavior) {
        List<String> labelsExpected = checkboxPage.getLabelsExpectedParentChildren(parent);
        List<String> listActualLabels = checkboxPage.getResultsLabels();

        SoftAssertions softAssert = new SoftAssertions ();

        if (expectedBehavior.equals("should be")) {
            for (String labelExpected : labelsExpected) {
                softAssert.assertThat(listActualLabels).as("El output debería contener el label seleccionado").contains(labelExpected);
            }
            softAssert.assertAll();
        }
        else if (expectedBehavior.equals("should not be")) {

            for (String labelExpected : labelsExpected) {
                softAssert.assertThat(listActualLabels).as("El output no debería contener el label seleccionado").doesNotContain(labelExpected);
            }
            softAssert.assertAll();
        }


    }

    @Then("the child and {word} should not be in the text output")
    public void validateChildAndParentNotBeOnTheTextOutput(String parent) {
        List<String> labelsExpected = checkboxPage.getLabelsExpectedLastChildParent(parent);
        List<String> listActualLabels = checkboxPage.getResultsLabels();

        SoftAssertions softAssert = new SoftAssertions ();
        for (String labelExpected : labelsExpected) {
            softAssert.assertThat(listActualLabels).as("El output no debería contener el label seleccionado").doesNotContain(labelExpected);
        }
        softAssert.assertAll();
    }



}
