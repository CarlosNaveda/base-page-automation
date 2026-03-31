package pages;

import driverManager.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BasePage {

    //VARIABLES
    private final WebDriver driver;
    private final WebDriverWait wait;

    //CONSTRUCTOR
    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    //MÉTODOS PRIVADOS
    private WebElement getWebElement(String locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
    }

    private List<WebElement> getOptionsSelect(String locator) {

        //Nos aseguramos que estén todas las opciones dentro
        WebElement select = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
        wait.until(driver -> select.findElements(By.tagName("option")).size() > 1);

        //Ahora si devolvemos la Lista las opciones del Select
        return select.findElements(By.tagName("option"));
    }

    //MÉTODOS PÚBLICOS

    //Ingresar a url
    public void navigateTo(String url) {
        driver.get(url);
    }

    //Hacer click en locator
    public void click(String locator) {
        getWebElement(locator).click();
    }

    //Obtener texto de un locator
    public String getTextWebElement(String locator) {
        return getWebElement(locator).getText();
    }

    //Obtener Lista de opciones de un Select
    public List<String> getListOptionsSelect (String locator) {
        List<String> list = new ArrayList<>();

        for (WebElement element : getOptionsSelect(locator)) {
            list.add(element.getText());
        }

        return list;
    }

    //Seleccionar opción de un Select
    public void selectOption(String locator, String option) {
        Select select = new Select(getWebElement(locator));
        select.selectByVisibleText(option);
    }









}
