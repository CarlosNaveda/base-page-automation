package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.elements.BrokenLinksPage;

public class BrokenLinksSteps {

    BrokenLinksPage brokenLinksPage = new BrokenLinksPage();
    String actualDescription;
    String previousUrl;

    @When("the user see the {string}")
    public void theUserSeeTheItem(String item) {
        actualDescription = brokenLinksPage.seeTheItem(item);
    }

    @When("the user click the {string}")
    public void theUserClickTheItem(String item) {
        previousUrl = brokenLinksPage.getWindowUrl();
        brokenLinksPage.clickTheItem(item);
    }

    @Then("can obtain the correct {string}")
    public void validateTheCorrectDescription(String expectedDescription) {
        Assert.assertEquals(actualDescription, expectedDescription);
    }

    @Then("can obtain the {string} of the changed page")
    public void validateTheChangedPageTitle(String expectedUrl) {
        String actualUrl = brokenLinksPage.getChangedPageUrl(previousUrl);
        Assert.assertEquals(actualUrl, expectedUrl);
    }
}
