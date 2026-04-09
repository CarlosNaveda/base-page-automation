package pages.forms;

import pages.BasePage;
import pages.elements.TextBoxPage;

public class FormsPage extends BasePage {

    public FormsPage() {
        super();
    }


    //Variables de opciones para Forms
    String practiceFormOption = "//span[normalize-space()='Practice Form']";

    //Métodos públicos
    public PracticeForm clickToPracticeFormOption()
    {
        click(practiceFormOption);
        return new PracticeForm();
    }

}
