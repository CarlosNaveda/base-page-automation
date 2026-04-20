package pages.forms;

import pages.BasePage;

public class FormsPage extends BasePage {

    public FormsPage() {
        super();
    }


    //Variables de opciones para Forms
    String practiceFormOption = "//span[normalize-space()='Practice Form']";

    //Métodos públicos
    public pages.forms.PracticeForm clickToPracticeFormOption()
    {
        click(practiceFormOption);
        return new PracticeForm();
    }

}
