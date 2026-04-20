package pages.bookStoreApplication;

import pages.BasePage;

public class BooksPage extends BasePage {

    public BooksPage() {
        super();
    }

    //Variables de opciones para Elements
    String loginOption = "//span[normalize-space()='Login']";
    String bookStoreOption = "//span[normalize-space()='Book Store']";
    String profileOption = "//span[normalize-space()='Profile']";
    String bookStoreApiOption = "//span[normalize-space()='Book Store API']";


    //Métodos públicos
    public LoginPage clickToLoginOption()
    {
        click(loginOption);
        return new LoginPage();
    }

    public pages.bookStoreApplication.BookStorePage clickToBookStoreOption()
    {
        click(bookStoreOption);
        return new BookStorePage();
    }

    public ProfilePage clickToProfileOption()
    {
        click(profileOption);
        return new ProfilePage();
    }

    public BookStoreApiPage clickToBookStoreApiOption()
    {
        click(bookStoreApiOption);
        return new BookStoreApiPage();
    }
}
