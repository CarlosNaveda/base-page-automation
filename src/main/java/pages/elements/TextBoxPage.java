package pages.elements;

import model.UserData;
import pages.BasePage;

public class TextBoxPage extends BasePage {

    public TextBoxPage() {
        super();
    }

    //Variables Text Box
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
