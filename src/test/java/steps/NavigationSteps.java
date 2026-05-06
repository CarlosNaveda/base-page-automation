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

    @Given("the user is on the links page")
    public void theUserIsOnTheLinksPage() {
        homePage.clickToHomePage();
        homePage.clickToElementsSection().clickToLinksOption();
    }


}
