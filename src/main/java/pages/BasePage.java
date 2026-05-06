package pages;

import driverManager.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    //MÉTODOS PRIVADOS

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
     * Nos dice si un locator está presente
     *
     * @param locator XPath del elemento web que queremos buscar
     * @return boolean true si está, caso contrario false
     */
    public boolean isLocatorPresent (String locator) {
        return !driver.findElements(By.xpath(locator)).isEmpty();
    }

    /**
     * Espera a que un elemento esté presente en el DOM y lo retorna
     *
     * @param locator XPath del elemento a buscar
     * @return WebElement encontrado en el DOM
     */
    public WebElement getWebElementPresent(String locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
    }


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
        return getWebElementVisible(locator).getText();
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
     * @param locatorIn xPath del elemento web a buscar dentro del webElement
     * @return WebElement elemento web buscado
     */
    public WebElement getWebElementInside(WebElement webElement, String locatorIn) {
        return webElement.findElement(By.xpath(locatorIn));
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

    public int generateRandomNumber (int max) {
        return (int) (Math.random() * max) + 1;
    }

    /**
     * Nos dice si un locator está habilitado
     *
     * @param locator XPath del elemento web que queremos buscar
     * @return boolean true si está, caso contrario false
     */
    public boolean isLocatorEnabled(String locator) {
        return driver.findElement(By.xpath(locator)).isEnabled();
    }

    /**
     * Hace click en el locator indicado pero de forma segura
     *
     * @param locator XPath del locator que queremos hacerle click
     *
     */
    public void safeClick(String locator){

        WebElement element = getWebElementVisible(locator);

        try { //Intento hacer el click normal con selenium
            element.click();
        } catch (Exception e) { //Sino hago click pero con Javascript
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    /**
     * Espera a que un elemento esté visible en el DOM y lo retorna
     *
     * @param locator XPath del elemento a buscar
     * @return WebElement encontrado en el DOM
     */
    public WebElement getWebElementVisible(String locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
    }

    /**
     * Valida si se ha realizado paginación comparando los textos del paginado
     *
     * @param locatorPage XPath texto del paginado
     * @param previousPage texto de la página previa
     */
    public void waitUntilPageChange(String locatorPage, String previousPage) {
        wait.until(driver -> {
            String current = getTextWebElement(locatorPage);
            if (current.equals(previousPage)) {
                return false;
            }
            System.out.println("Página cambió de: " + previousPage + " a " + current);
            return true;
        });
    }

    /**
     * Hace click derecho en el locator indicado
     *
     * @param locator XPath del locator que queremos hacerle click derecho
     *
     */
    public void rightClick(String locator) {
        WebElement element = getWebElementClickable(locator);
        Actions actions = new Actions(driver);
        actions.contextClick(element).perform();
    }

    /**
     * Hace doble click en el locator indicado
     *
     * @param locator XPath del locator que queremos hacerle doble click
     *
     */
    public void doubleClick(String locator) {
        WebElement element = getWebElementClickable(locator);
        Actions actions = new Actions(driver);
        actions.doubleClick(element).perform();
    }

    //En TEST (Aquí irán los métodos que aún estoy probando)
    public String getActualWindowHandle() {
        return driver.getWindowHandle();
    }

    public boolean isNewWindow(String actualWindowHandle) {
        //Esperamos un tiempo a que sean 2 ventanas
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(driver -> driver.getWindowHandles().size() > 1);
        //Cambiamos de ventana
        for (String windowHandle : driver.getWindowHandles()) {
            if (!windowHandle.equals(actualWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        //Retornamos si llegan a ser distintas ventanas
        return !actualWindowHandle.equals(driver.getWindowHandle());
    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }

    public String getTitle() {
        return driver.getTitle();
    }











}
