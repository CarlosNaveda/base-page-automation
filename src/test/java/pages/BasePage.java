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

    /**
     * Espera a que un elemento esté presente en el DOM y lo retorna
     *
     * @param locator XPath del elemento a buscar
     * @return WebElement encontrado en el DOM
     */
    private WebElement getWebElementPresent(String locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
    }

    /**
     * Espera a que un elemento esté disponible para hacer click en el DOM y lo retorna
     *
     * @param locator XPath del elemento a buscar
     * @return WebElement encontrado en el DOM
     */
    private WebElement getWebElementClickable(String locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
    }

    /**
     * Genera una lista de WebElements en base al Select del DOM y lo retorna
     *
     * @param locator XPath del Select a extraer las opciones
     * @return List<WebElement> armado con las opciones
     */
    private List<WebElement> getOptionsSelect(String locator) {

        //Nos aseguramos que estén todas las opciones dentro
        WebElement select = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
        wait.until(driver -> select.findElements(By.tagName("option")).size() > 1);

        //Ahora si devolvemos la Lista las opciones del Select
        return select.findElements(By.tagName("option"));
    }

    //MÉTODOS PÚBLICOS

    /**
     * Ingresa a URL en el navegador
     *
     * @param url Dirección web a la cual queremos dirigirnos
     *
     */
    public void navigateTo(String url) {
        driver.get(url);
    }

    /**
     * Hace click en el locator indicado
     *
     * @param locator XPath del locator que queremos hacerle click
     *
     */
    public void click(String locator) {
        getWebElementClickable(locator).click();
    }

    /**
     * Obtiene el texto de un elemento web del DOM
     *
     * @param locator XPath del locator que queremos su texto
     * @return String del texto en base al locator
     */
    public String getTextWebElement(String locator) {
        return getWebElementPresent(locator).getText();
    }

    /**
     * Escribir texto en el elemento web del DOM
     *
     * @param locator XPath del locator que queremos escribir
     * @param value Texto que queremos escribir
     *
     */
    public void setValueOnWebElement(String locator, String value) {
        WebElement element = getWebElementClickable(locator);
        element.clear();
        element.sendKeys(value);
    }


    /**
     * Genera una lista de Strings en base al Select del DOM y lo retorna
     *
     * @param locator XPath del Select a extraer las opciones
     * @return List<String> armado con las opciones
     */
    public List<String> getListOptionsSelect (String locator) {
        List<String> list = new ArrayList<>();

        for (WebElement element : getOptionsSelect(locator)) {
            list.add(element.getText());
        }

        return list;
    }

    /**
     * Selecciona una opción dentro de un Select de un elemento Web
     *
     * @param locator XPath del Select para elegir la opción
     * @param option String que indica la opción que vamos a elegir en el Select
     */
    public void selectOption(String locator, String option) {
        Select select = new Select(getWebElementPresent(locator));
        select.selectByVisibleText(option);
    }









}
