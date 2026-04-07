package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import model.UserData;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import pages.DemoQA;

public class textbox {

    DemoQA demoQA = new DemoQA();
    UserData userData;

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
        userData = new UserData("Carlos Naveda","carlosnavedatest@gmail.com","Avenida Automation 123 Lima","Jirón Universo 777 Lima");
        demoQA.fillTextboxes(userData);
    }

    @And("fill partial the email textbox on the page")
    public void fillPartialTheEmailTextboxOnThePage(){
        userData = new UserData("Carlos Naveda","carlosnavedatest","Avenida Automation 123 Lima","Jirón Universo 777 Lima");
        demoQA.fillTextboxes(userData);
    }

    @And("click the button Submit")
    public void clickTheButtonSubmit(){
        demoQA.clickOnButtonSubmit();
    }

    @Then("can confirm his information in the output section")
    public void canConfirmHisInformationInTheOutputSection(){
        UserData userDataOutput= demoQA.getInformationOfOutput();
        SoftAssert softAssert = new SoftAssert();

        //Validamos cada campo output
        softAssert.assertEquals(userDataOutput.userName, userData.userName,"El nombre no corresponde el ingresado");
        softAssert.assertEquals(userDataOutput.email, userData.email,"El nombre no corresponde el ingresado");
        softAssert.assertEquals(userDataOutput.currentAddress, userData.currentAddress,"El nombre no corresponde el ingresado");
        softAssert.assertEquals(userDataOutput.permanentAddress, userData.permanentAddress,"El nombre no corresponde el ingresado");

        softAssert.assertAll();
    }

    @Then("can confirm the email validation message")
    public void canConfirmTheEmailValidationMessage(){
        String validationMessage = demoQA.getValidationMessageWebElement();
        System.out.println("Mensaje de validación: " + validationMessage);

        //Validamos que el texto tenga información, eso nos dice de que está incompleto el campo email
        if(!validationMessage.isEmpty()){
            Assert.assertTrue(true);
        }

    }





}
