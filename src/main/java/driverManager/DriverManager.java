package driverManager;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void initDriverManager() {
        if (DRIVER.get() == null) {
            WebDriverManager.chromedriver().setup();
            DRIVER.set(new ChromeDriver());
            DRIVER.get().manage().window().maximize();
        }
    }

    public static void quitDriverManager() {
        if (DRIVER.get() != null) {
            DRIVER.get().quit();
            DRIVER.remove();
        }
    }


}
