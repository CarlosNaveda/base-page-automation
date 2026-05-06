package pages.elements;

import pages.BasePage;

public class LinksPage extends BasePage {

    public LinksPage() {
        super();
    }

    //Variables
    String simpleLinkHome = "//a[@id='simpleLink']";
    String dynamicLinkHome = "//a[@id='dynamicLink']";
    String createdLink = "//a[@id='created']";
    String noContentLink = "//a[@id='no-content']";
    String movedLink = "//a[@id='moved']";
    String badRequestLink = "//a[@id='bad-request']";
    String unauthorizedLink = "//a[@id='unauthorized']";
    String ForbiddenLink = "//a[@id='forbidden']";
    String NotFoundLink = "//a[@id='invalid-url']";
    String numberResponse = "//b[1]";
    String textResponse = "//b[2]";
    String linkResponse = "//b[normalize-space()='%s']";

    //Métodos privados

    //Métodos públicos
    public void clickTheNewTabLink(String link) {

        switch(link) {
            case "Simple Link":
                click(simpleLinkHome);
                break;

            case "Dynamic Link":
                click(dynamicLinkHome);
                break;
        }
    }

    public void clickTheApiCallLink(String link) {
        switch(link) {

            case "Created Link":
                click(createdLink);
                break;

            case "No Content Link":
                click(noContentLink);
                break;

            case "Moved Link":
                click(movedLink);
                break;

            case "Bad Request Link":
                click(badRequestLink);
                break;

            case "Unauthorized Link":
                click(unauthorizedLink);
                break;

            case "Forbidden Link":
                click(ForbiddenLink);
                break;

            case "Not Found Link":
                click(NotFoundLink);
                break;
        }
    }

    public String getWindowHandle() {
        return getActualWindowHandle();
    }

    public String getNewPageUrl(String actualWindowHandle) {
        String newUrl = "";
        if(isNewWindow(actualWindowHandle)) { //Si es una nueva ventana
            newUrl = getUrl();
        }

        return newUrl;
    }

    public int getActualNumber(){
        return Integer.parseInt(getTextWebElement(numberResponse));
    }

    public String getActualText(){
        return getTextWebElement(textResponse);
    }

}
