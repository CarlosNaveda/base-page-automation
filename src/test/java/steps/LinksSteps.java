package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.SoftAssertions;
import org.testng.Assert;
import pages.elements.LinksPage;

public class LinksSteps {

    private String actualWindowHandle;
    LinksPage linksPage =  new LinksPage();

    @When("the user click the new tab link {string}")
    public void theUserClickTheNewTabLink(String button) {
        actualWindowHandle = linksPage.getWindowHandle(); //Capturo la ventana actual antes de dar click
        linksPage.clickTheNewTabLink(button);
    }

    @When("the user click the api call link {string}")
    public void theUserClickTheApiCallLink(String button) {
        linksPage.clickTheApiCallLink(button);
    }

    @Then("The new page have the {string} correct")
    public void ValidateTheNewPageCorrectUrl(String expectedUrl) {
        String actualURL = linksPage.getNewPageUrl(actualWindowHandle);
        Assert.assertEquals(actualURL, expectedUrl);
    }

    @Then("The page show the {int} and {string}")
    public void ValidateThePageNumberTextResponse(int expectedNumber, String expectedText) {
        int actualNumber = linksPage.getActualNumber();
        String actualText = linksPage.getActualText();

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(actualNumber).isEqualTo(expectedNumber);
        softAssertions.assertThat(actualText).isEqualTo(expectedText);
        softAssertions.assertAll();

    }


}
