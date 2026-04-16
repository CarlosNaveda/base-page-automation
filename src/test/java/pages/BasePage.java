package pages;

import driverManager.DriverManager;
import model.CheckboxNode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import valueObject.CheckboxState;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BasePage {

    //VARIABLES
    private final WebDriver driver;
    private final WebDriverWait wait;

    //CONSTRUCTOR
    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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

    /**
     * Nos dice si un locator está presente
     *
     * @param locator XPath del elemento web que queremos buscar
     * @return boolean true si está, caso contrario false
     */
    private boolean isLocatorPresent (String locator) {
        return !driver.findElements(By.xpath(locator)).isEmpty();
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

    /**
     * Extrae el tooltip de mensaje de validación que nos devuelve un elemento web
     *
     * @param locator XPath del elemento web para extraer el tooltip
     * @return String texto del tooltip
     */
    public String getValidationMessageWebElement (String locator) {
        return getWebElementPresent(locator).getDomProperty("validationMessage");
    }

    /**
     * Devuelve el estado de un checkbox: seleccionado, no seleccionado o indeterminado
     *
     * @param checkboxWebElement elemento web de checkbox
     * @return CheckboxState estado del checkbox
     */
    public CheckboxState getStateOfCheckbox (WebElement checkboxWebElement) {
        String state = checkboxWebElement.getAttribute("aria-checked");
        CheckboxState checkboxState = null;
        switch (state) {
            case "true":
                checkboxState = CheckboxState.SELECTED;
                break;
            case "false":
                checkboxState = CheckboxState.NOT_SELECTED;
                break;
            case "mixed":
                checkboxState = CheckboxState.INDETERMINATE;
                break;
            case null:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + state);
        }
        return checkboxState;
    }

    /**
     * Hace click en todos los locators que encuentre con ese xpath
     *
     * @param locator XPath de los locators que queremos hacerle click
     *
     */
    public void clickAll(String locator) {

        //Cuando es la primera vez, reviso si está presente el locator
        if(isLocatorPresent(locator)){
            //Mientras aún exista elementos para darle click
            do {
                click(locator); //Aquí cambia el DOM
            } while (isLocatorPresent(locator));
        }
    }

    /**
     * Abre todos los switcher + cerrados visualizados en la página
     *
     * @param locator XPath del elemento web switcher
     */
    public void openAllCloseSwitcher (String locator) {
        String SwitcherCloseLocator = String.format(locator, "close");
        clickAll(SwitcherCloseLocator);

    }

    /**
     * Devuelve todos los elementos web que coincidan con el locator, no espera
     *
     * @param locator xPath del elemento web a buscar
     * @return List<WebElement> listado de elementos encontrados a devolver
     */
    public List<WebElement> getAllWebElements(String locator) {
        return driver.findElements(By.xpath(locator));
    }


    /**
     * Devuelve el elemento web que coincidan con el locator pero dentro de otro elemento web
     *
     * @param webElement elemento web padre
     * @param locator xPath del elemento web a buscar
     * @return WebElement elemento web buscado
     */
    public WebElement getWebElementInside(WebElement webElement, String locator) {
        return webElement.findElement(By.xpath(locator));
    }

    /**
     * Devuelve el elemento web que coincidan con el locator
     *
     * @param locator xPath del elemento web a buscar
     * @return WebElement elemento web buscado, si no existe devuelvo null
     */
    public WebElement getWebElement(String locator) {
        if (isLocatorPresent(locator)) {
            return driver.findElement(By.xpath(locator));
        }
        else
        {   return null;
        }
    }


    //En TEST (Aquí irán los métodos que aún estoy probando)










}
