package pages;

import model.UserData;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class DemoQA extends BasePage {

    //Constructor
    public DemoQA(){
        super();
    }

    //Variable URL de la página DEMOQA
    String url = "https://demoqa.com/";

    //Variables secciones de la página DEMOQA
    String elementsSection = "//h5[normalize-space()='Elements']";
    String formsSection = "//h5[normalize-space()='Forms']";
    String alertFrameWindowsSection = "//h5[normalize-space()='Alerts, Frame & Windows']";
    String widgetsSection = "//h5[normalize-space()='Widgets']";
    String interactionsSection = "//h5[normalize-space()='Interactions']";
    String bookStoreApplicationSection = "//h5[normalize-space()='Book Store Application']";

    //Variables de acordión en la página DEMOQA
    String textboxAccordionOption = "//span[normalize-space()='Text Box']";

    //Variables sección Elements / Text Box
    String fullNameTextBox = "//input[@id='userName']";
    String emailTextBox = "//input[@id='userEmail']";
    String currentAddressTextBox = "//textarea[@id='currentAddress']";
    String permanentAddressTextBox = "//textarea[@id='permanentAddress']";
    String submitButton = "//button[@id='submit']";
    String fullNameOutput = "//p[@id='name']";
    String emailOutput = "//p[@id='email']";
    String currentAddressOutput = "//p[@id='currentAddress']";
    String permanentAddressOutput = "//p[@id='permanentAddress']";

    //Métodos privados
    private String extractValueText(String outputText){
        String[] array =  outputText.split(":",2);
        return array.length > 1 ? array[1].trim() : outputText.trim();
    }


    //Métodos públicos
    //Método para ir a la página y elegir sección
    public void GoToThePageDemoQa(){
        navigateTo(url);
    }

    public void clickOnElementsSection()
    {
        click(elementsSection);
    }

    //Método para Text Box
    public void clickOnAccordionTextBoxOption()
    {
        click(textboxAccordionOption);
    }

    public void fillTextboxes (UserData userData){
        setValueOnWebElement(fullNameTextBox,userData.userName);
        setValueOnWebElement(emailTextBox,userData.email);
        setValueOnWebElement(currentAddressTextBox,userData.currentAddress);
        setValueOnWebElement(permanentAddressTextBox,userData.permanentAddress);
    }

    public void clickOnButtonSubmit()
    {
        click(submitButton);
    }

    public UserData getInformationOfOutput(){
        return new UserData(
            extractValueText(getTextWebElement(fullNameOutput)),
            extractValueText(getTextWebElement(emailOutput)),
            extractValueText(getTextWebElement(currentAddressOutput)),
            extractValueText(getTextWebElement(permanentAddressOutput))
        );
    }

    public String getValidationMessageWebElement(){
        return getValidationMessageWebElement(emailTextBox);
    }




}
