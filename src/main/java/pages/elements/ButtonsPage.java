package pages.elements;

import pages.BasePage;

public class ButtonsPage extends BasePage {

    public ButtonsPage() {
        super();
    }

    //Variables
    String doubleClickButton = "//button[@id='doubleClickBtn']";
    String rightClickButton = "//button[@id='rightClickBtn']";
    String clickMeButton = "//button[normalize-space()='Click Me']"; //Su id es dinámico
    String doubleClickMessage = "//p[@id='doubleClickMessage']";
    String rightClickMessage = "//p[@id='rightClickMessage']";
    String clickMeMessage = "//p[@id='dynamicClickMessage']";

    //Métodos privados


    //Métodos públicos
    public void clickTheButton(String button) {

        switch(button) {
            case "Double Click Me":
                doubleClick(doubleClickButton);
                break;

            case "Right Click Me":
                rightClick(rightClickButton);
                break;

            case "Click Me":
                click(clickMeButton);
                break;
        }
    }

    public void clickMixedButtons(String numberButtons) {
        String[] buttons = numberButtons.split(",");
        for (String button : buttons) {
            switch(button.trim()) {
                case "1":
                    doubleClick(doubleClickButton);
                    break;

                case "2":
                    rightClick(rightClickButton);
                    break;

                case "3":
                    click(clickMeButton);
                    break;
            }
        }
    }



    public String getMessageInPage(String buttonPressed){

        return switch (buttonPressed) {
            case "Double Click Me" -> getTextWebElement(doubleClickMessage);
            case "Right Click Me" -> getTextWebElement(rightClickMessage);
            case "Click Me" -> getTextWebElement(clickMeMessage);
            default -> "";
        };

    }

    public String getMixedMessageInPage(String buttonPressed){
        return switch (buttonPressed) {
            case "1" -> getTextWebElement(doubleClickMessage);
            case "2" -> getTextWebElement(rightClickMessage);
            case "3" -> getTextWebElement(clickMeMessage);
            default -> "";
        };
    }



}
