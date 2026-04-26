package pages.elements;

import org.testng.Assert;
import pages.BasePage;
import static org.assertj.core.api.Assertions.assertThat;

public class RadioButtonPage extends BasePage {

    public RadioButtonPage() {
        super();
    }

    //Variables
    String radioButtonYes= "//input[@id='yesRadio']";
    String radioButtonImpressive= "//input[@id='impressiveRadio']";
    String radioButtonNo= "//input[@id='noRadio']";
    String textSuccess= "//span[@class='text-success']";


    //Métodos privados

    //Métodos públicos
    public void selectRadioButton(String radioButtonName) {

        switch(radioButtonName){
            case "Yes":
                click(radioButtonYes);
                break;
            case "Impressive":
                click(radioButtonImpressive);
                break;
            case "No":
                if (isLocatorEnabled(radioButtonNo)){
                    click(radioButtonNo);
                }
                break;
        }

    }

    public void validateTextSuccess(String textOutputExpected) {
        String textOutputActual = getTextWebElement(textSuccess);
        Assert.assertEquals(textOutputActual, textOutputExpected);
    }

    public void validateNoExistTextSuccess(){
        Boolean isTextSuccessExist = isLocatorPresent(textSuccess);
        assertThat(isTextSuccessExist).isEqualTo(false);
    }






}
