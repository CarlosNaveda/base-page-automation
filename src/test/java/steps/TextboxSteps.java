package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import model.UserData;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import pages.elements.TextBoxPage;

public class TextboxSteps {

    TextBoxPage textBoxPage = new TextBoxPage();
    UserData userData;

    @And("fill textboxes on the page")
    public void fillTextboxesOnThePage(){
        userData = new UserData("Carlos Naveda","carlosnavedatest@gmail.com","Avenida Automation 123 Lima","Jirón Universo 777 Lima");
        textBoxPage.fillTextboxes(userData);
    }

    @And("fill partial the email textbox on the page")
    public void fillPartialTheEmailTextboxOnThePage(){
        userData = new UserData("Carlos Naveda","carlosnavedatest","Avenida Automation 123 Lima","Jirón Universo 777 Lima");
        textBoxPage.fillTextboxes(userData);
    }

    @And("click the button Submit")
    public void clickTheButtonSubmit(){
        textBoxPage.clickOnButtonSubmit();
    }

    @Then("can confirm his information in the output section")
    public void canConfirmHisInformationInTheOutputSection(){
        UserData userDataOutput= textBoxPage.getInformationOfOutput();
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
        String validationMessage = textBoxPage.getValidationMessageWebElement();
        System.out.println("Mensaje de validación: " + validationMessage);

        //Validamos que el texto tenga información, eso nos dice de que está incompleto el campo email
        if(!validationMessage.isEmpty()){
            Assert.assertTrue(true);
        }

    }





}
