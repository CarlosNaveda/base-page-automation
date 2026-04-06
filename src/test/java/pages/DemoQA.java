package pages;

import org.openqa.selenium.WebDriver;

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

    //Dummy Data
    String userName = "Carlos Naveda";
    String email = "carlosnavedatest@gmail.com";
    String currentAddress = "Avenida Automation 123 Lima";
    String permanentAddress = "Jirón Universo 777 Lima";



    //Métodos
    public void GoToThePageDemoQa(){
        navigateTo(url);
    }

    public void clickOnElementsSection()
    {
        click(elementsSection);
    }

    public void clickOnAccordionTextBoxOption()
    {
        click(textboxAccordionOption);
    }

    public void fillTextboxes (){
        setValueOnWebElement(fullNameTextBox,userName);
        setValueOnWebElement(emailTextBox,email);
        setValueOnWebElement(currentAddressTextBox,currentAddress);
        setValueOnWebElement(permanentAddressTextBox,permanentAddress);
    }




}
