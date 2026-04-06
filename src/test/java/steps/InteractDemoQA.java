package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import pages.DemoQA;

public class InteractDemoQA {

    DemoQA demoQA = new DemoQA();

    @Given("the user go to the page DemoQa")
    public void theUserGoToThePageDemoQa(){
        demoQA.GoToThePageDemoQa();
    }

    @When("click on the elements section")
    public void clickOnTheElementsSection(){
        demoQA.clickOnElementsSection();
    }

    @And("click on the text box accordion option")
    public void clickOnTheTextBoxAccordionOption(){
        demoQA.clickOnAccordionTextBoxOption();
    }

    @And("fill textboxes on the page")
    public void fillTextboxesOnThePage(){
        demoQA.fillTextboxes();
    }






}
