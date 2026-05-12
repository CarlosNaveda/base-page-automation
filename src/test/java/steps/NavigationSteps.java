package steps;

import io.cucumber.java.en.Given;
import pages.HomePage;

public class NavigationSteps {

    HomePage homePage = new HomePage();

    @Given("the user is on the text box page")
    public void goToTextBoxPage() {
        homePage.clickToHomePage();
        homePage.clickToElementsSection().clickToTextBoxOption();
    }

    @Given("the user is on the check box page")
    public void goToCheckBoxPage() {
        homePage.clickToHomePage();
        homePage.clickToElementsSection().clickToCheckboxOption();
    }

    @Given("the user is on the radio button page")
    public void goToRadioButtonPage() {
        homePage.clickToHomePage();
        homePage.clickToElementsSection().clickToRadioButtonOption();
    }

    @Given("the user is on the web tables page")
    public void goToWebTablesPage() {
        homePage.clickToHomePage();
        homePage.clickToElementsSection().clickToWebTablesOption();
    }

    @Given("the user is on the buttons page")
    public void theUserIsOnTheButtonsPage() {
        homePage.clickToHomePage();
        homePage.clickToElementsSection().clickToButtonsOption();
    }

    @Given("the user is on the links page and close extra windows")
    public void theUserIsOnTheLinksPageAndCloseExtraWindows() {
        homePage.clickToHomePage();
        homePage.clickToElementsSection().clickToLinksOption().closeExtraWindows();
    }

    @Given("The user is on the broken links page")
    public void theUserIsOnTheBrokenLinksPage() {
        homePage.clickToHomePage();
        homePage.clickToElementsSection().clickToBrokenLinksOption();
    }

    @Given("the user is on the uploadDownload page")
    public void theUserIsOnTheUploadDownloadPage() {
        homePage.clickToHomePage();
        homePage.clickToElementsSection().clickToUploadDownloadOption();
    }


}
