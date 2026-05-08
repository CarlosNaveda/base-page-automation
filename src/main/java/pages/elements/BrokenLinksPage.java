package pages.elements;

import pages.BasePage;

public class BrokenLinksPage extends BasePage {

    public BrokenLinksPage() {
        super();
    }

    //Variables
    String validImage = "//img[@src='/images/Toolsqa.jpg']";
    String invalidImage = "//img[@src='/images/Toolsqa_1.jpg']";
    String validLink = "//a[normalize-space()='Click Here for Valid Link']";
    String invalidLink = "//a[normalize-space()='Click Here for Broken Link']";
    String precedingP = "/preceding-sibling::p[1]"; //Uso índice porque solo quiero el p inmediato anterior

    //Métodos privados

    //Métodos públicos
    public String seeTheItem(String item) {

        return switch (item) {
            case "Valid image" -> getTextWebElement(validImage+precedingP);
            case "Invalid image" -> getTextWebElement(invalidImage+precedingP);
            case "Valid link" -> getTextWebElement(validLink+precedingP);
            case "Broken link" -> getTextWebElement(invalidLink+precedingP);
            default -> "";
        };
    }

    public void clickTheItem(String item) {
        switch (item) {
            case "Valid link" -> click(validLink);
            case "Broken link" -> click(invalidLink);
        }
    }


    public String getChangedPageUrl(String previousUrl) {

        String newUrl = "";

        if(isWindowUrlChanged(previousUrl)) { //Si el url de la ventana cambió
            newUrl = getWindowUrl();
        }

        return newUrl;
    }

}
