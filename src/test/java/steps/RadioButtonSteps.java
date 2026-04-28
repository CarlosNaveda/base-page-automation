package steps;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.elements.RadioButtonPage;
import static org.assertj.core.api.Assertions.assertThat;

public class RadioButtonSteps {

    RadioButtonPage radioButtonPage = new RadioButtonPage();

    @When("the radiobutton {word} is selected")
    public void theRadiobuttonIsSelected(String radioButtonName) {
        radioButtonPage.selectRadioButton(radioButtonName);
    }

    @Then("the text success show the {word}")
    public void validateRadioNameShowOnTextSuccess(String textExpected) {
        String textSuccess = radioButtonPage.getTextSuccess();
        Assert.assertEquals(textSuccess, textExpected);
    }

    @Then("the text success doesn't show")
    public void validateRadioNameNotShowOnTextSuccess() {
        Boolean isTextSuccessExist = radioButtonPage.isTextSuccessExist();
        assertThat(isTextSuccessExist).isEqualTo(false);
    }

}
