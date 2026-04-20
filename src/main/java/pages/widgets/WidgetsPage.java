package pages.widgets;

import pages.BasePage;

public class WidgetsPage extends BasePage {

    public WidgetsPage() {
        super();
    }

    //Variables de opciones para Widgets
    String accordianOption = "//span[normalize-space()='Accordian']";
    String autoCompleteOption = "//span[normalize-space()='Auto Complete']";
    String datePickerOption = "//span[normalize-space()='Date Picker']";
    String sliderOption = "//span[normalize-space()='Slider']";
    String progressBarOption = "//span[normalize-space()='Progress Bar']";
    String tabsOption = "//span[normalize-space()='Tabs']";
    String tooltipsOption = "//span[normalize-space()='Tool Tips']";
    String menuOption = "//span[normalize-space()='Menu']";
    String selectMenuOption = "//span[normalize-space()='Select Menu']";

    //Métodos públicos
    public AccordianPage clickToAccordianOption()
    {
        click(accordianOption);
        return new AccordianPage();
    }

    public AutoCompletePage clickToAutoCompleteOption()
    {
        click(autoCompleteOption);
        return new AutoCompletePage();
    }

    public DatePickerPage clickToDatePickerOption()
    {
        click(datePickerOption);
        return new DatePickerPage();
    }

    public SliderPage clickToSliderOption()
    {
        click(sliderOption);
        return new SliderPage();
    }

    public ProgressBarPage clickToProgressBarOption()
    {
        click(progressBarOption);
        return new ProgressBarPage();
    }

    public TabsPage clickToTabsOption()
    {
        click(tabsOption);
        return new TabsPage();
    }

    public TooltipsPage clickToTooltipsOption()
    {
        click(tooltipsOption);
        return new TooltipsPage();
    }

    public pages.widgets.MenuPage clickToMenuOption()
    {
        click(menuOption);
        return new MenuPage();
    }

    public SelectMenuPage clickToSelectMenuOption()
    {
        click(selectMenuOption);
        return new SelectMenuPage();
    }


}
