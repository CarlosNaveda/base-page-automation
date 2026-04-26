package steps;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.elements.RadioButtonPage;

public class RadioButtonSteps {

    RadioButtonPage radioButtonPage = new RadioButtonPage();

    @When("the radiobutton {word} is selected")
    public void theRadiobuttonIsSelected(String radioButtonName) {
        radioButtonPage.selectRadioButton(radioButtonName);
    }

    @Then("the text success show the {word}")
    public void theTextSuccessShowTheText(String textOutput) {
        radioButtonPage.validateTextSuccess(textOutput);

    }

    @Then("the text success doesn't show")
    public void theTextSuccessDoesntShowTheText() {
        radioButtonPage.validateNoExistTextSuccess();
    }



}
