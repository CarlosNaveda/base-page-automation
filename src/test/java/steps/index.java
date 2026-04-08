package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import pages.DemoQA;

public class index {

    DemoQA demoQA = new DemoQA();

    @Given("the user goes to the page DemoQa")
    public void theUserGoToThePageDemoQa(){
        demoQA.GoToThePageDemoQa();
    }

    @When("selects the elements section")
    public void clickOnTheElementsSection(){
        demoQA.clickOnElementsSection();
    }
}
