package pages.alertFrame;

import pages.BasePage;
import pages.forms.PracticeForm;

public class AlertsWindowsPage extends BasePage {

    public AlertsWindowsPage() {
        super();
    }

    //Variables de opciones para Alerts, Frame & Windows
    String browserWindowsOption = "//span[normalize-space()='Browser Windows']";
    String alertsOption = "//span[normalize-space()='Alerts']";
    String framesOption = "//span[normalize-space()='Frames']";
    String nestedFramesOption = "//span[normalize-space()='Nested Frames']";
    String modalDialogOption = "//span[normalize-space()='Modal Dialogs']";

    //Métodos públicos
    public BrowserWindowsPage clickToBrowserWindowsOption()
    {
        click(browserWindowsOption);
        return new BrowserWindowsPage();
    }

    public AlertsPage clickToAlertsOption()
    {
        click(alertsOption);
        return new AlertsPage();
    }

    public FramesPage clickToFramesOption()
    {
        click(framesOption);
        return new FramesPage();
    }

    public NestedFramesPage clickToNestedFramesOption()
    {
        click(nestedFramesOption);
        return new NestedFramesPage();
    }

    public ModalDialogPage clickToModalDialogOption()
    {
        click(modalDialogOption);
        return new ModalDialogPage();
    }


}
