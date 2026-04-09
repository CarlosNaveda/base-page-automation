package pages;

import pages.alertFrame.AlertsWindowsPage;
import pages.bookStoreApplication.BooksPage;
import pages.elements.ElementsPage;
import pages.forms.FormsPage;
import pages.interactions.InteractionPage;
import pages.widgets.WidgetsPage;

public class HomePage extends BasePage {

   public HomePage() {
        super();
    }

    //Variable URL de la página DemoQA
    String homePageURL = "https://demoqa.com/";

    //Variables secciones de la página DEMOQA
    String elementsSection = "//h5[normalize-space()='Elements']";
    String formsSection = "//h5[normalize-space()='Forms']";
    String alertFrameWindowsSection = "//h5[normalize-space()='Alerts, Frame & Windows']";
    String widgetsSection = "//h5[normalize-space()='Widgets']";
    String interactionsSection = "//h5[normalize-space()='Interactions']";
    String bookStoreApplicationSection = "//h5[normalize-space()='Book Store Application']";


    //Métodos públicos
    public void clickToHomePage(){
        navigateTo(homePageURL);
    }

    public ElementsPage clickToElementsSection()
    {
        click(elementsSection);
        return new ElementsPage();
    }

    public FormsPage clickToFormsSection()
    {
        click(formsSection);
        return new FormsPage();
    }

    public AlertsWindowsPage clickToAlertFrameWindowsSection()
    {
        click(alertFrameWindowsSection);
        return new AlertsWindowsPage();
    }

    public WidgetsPage clickToWidgetsSection()
    {
        click(widgetsSection);
        return new WidgetsPage();
    }

    public InteractionPage clickToInteractionsSection()
    {
        click(interactionsSection);
        return new InteractionPage();
    }

    public BooksPage clickToBookStoreApplicationSection()
    {
        click(bookStoreApplicationSection);
        return new BooksPage();
    }



}
