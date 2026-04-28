package pages.elements;

import pages.BasePage;

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

    public String getTextSuccess() {
        return getTextWebElement(textSuccess);
    }

    public boolean isTextSuccessExist(){
        return isLocatorPresent(textSuccess);
    }






}
