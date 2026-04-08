package steps;

import io.cucumber.java.en.And;
import pages.DemoQA;

public class options {

    DemoQA demoQA = new DemoQA();

    @And("selects the text box accordion option")
    public void clickOnTheTextBoxAccordionOption(){
        demoQA.clickOnAccordionTextBoxOption();
    }

}
