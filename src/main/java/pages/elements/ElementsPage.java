package pages.elements;

import pages.BasePage;

public class ElementsPage extends BasePage {

    //Constructor
    public ElementsPage(){
        super();
    }

    //Variables de opciones para Elements
    String textboxOption = "//span[normalize-space()='Text Box']";
    String checkboxOption = "//span[normalize-space()='Check Box']";
    String radioButtonOption = "//span[normalize-space()='Radio Button']";
    String webTablesOption = "//span[normalize-space()='Web Tables']";
    String buttonsOption = "//span[normalize-space()='Buttons']";
    String linksOption = "//span[normalize-space()='Links']";
    String brokenLinksOption = "//span[normalize-space()='Broken Links - Images']";
    String uploadDownloadOption = "//span[normalize-space()='Upload and Download']";
    String dynamicPropertiesOption = "//span[normalize-space()='Dynamic Properties']";

    //Métodos públicos
    public pages.elements.TextBoxPage clickToTextBoxOption()
    {
        click(textboxOption);
        return new TextBoxPage();
    }

    public CheckboxPage clickToCheckboxOption()
    {
        click(checkboxOption);
        return new CheckboxPage();
    }

    public RadioButtonPage clickToRadioButtonOption()
    {
        click(radioButtonOption);
        return new RadioButtonPage();
    }

    public WebTablesPage clickToWebTablesOption()
    {
        click(webTablesOption);
        return new WebTablesPage();
    }

    public ButtonsPage clickToButtonsOption()
    {
        click(buttonsOption);
        return new ButtonsPage();
    }

    public LinksPage clickToLinksOption()
    {
        click(linksOption);
        return new LinksPage();
    }

    public BrokenLinksPage clickToBrokenLinksOption()
    {
        click(brokenLinksOption);
        return new BrokenLinksPage();
    }

    public UploadDownloadPage clickToUploadDownloadOption()
    {
        click(uploadDownloadOption);
        return new UploadDownloadPage();
    }

    public DynamicPropertiesPage clickToDynamicPropertiesOption()
    {
        click(dynamicPropertiesOption);
        return new DynamicPropertiesPage();
    }





}
