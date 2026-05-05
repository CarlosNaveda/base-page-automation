package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.SoftAssertions;
import org.testng.Assert;
import pages.elements.ButtonsPage;
import java.util.Map;

public class ButtonsSteps {

    ButtonsPage buttonsPage = new ButtonsPage();

    private final Map<String,String> MAP_MESSAGE = Map.ofEntries(
            Map.entry("1", "You have done a double click"),
            Map.entry("2", "You have done a right click"),
            Map.entry("3", "You have done a dynamic click")
    );


    private String getExpectedMessage(String buttonPressed){
        return MAP_MESSAGE.get(buttonPressed);
    }

    @When("the user click the {string}")
    public void theUserClickTheButton(String button) {
        buttonsPage.clickTheButton(button);
    }

    @When("the user click the buttons {string}")
    public void theUserClickMixedButtons(String numberButtons) {
        buttonsPage.clickMixedButtons(numberButtons);
    }

    @Then("The {string} should be correct based on the {string} pressed")
    public void validateButtonMessage(String expectedMessage,String buttonPressed) {
        String actualMessage = buttonsPage.getMessageInPage(buttonPressed);
        Assert.assertEquals(actualMessage,expectedMessage);
    }

    @Then("The messages should be correct based on the buttons {string} pressed")
    public void validateButtonsMessage(String numberButtonsPressed) {
        String[] buttons = numberButtonsPressed.split(",");
        SoftAssertions softAssertions = new SoftAssertions();

        for (String button : buttons) {
            String buttonPressed = button.trim();
            String actualMessage = buttonsPage.getMixedMessageInPage(buttonPressed);
            String expectedMessage = getExpectedMessage(buttonPressed);
            softAssertions.assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        softAssertions.assertAll();
    }

}
