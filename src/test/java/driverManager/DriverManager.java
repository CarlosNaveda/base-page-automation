package driverManager;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverManager {

    private static WebDriver driver;

    public static WebDriver getDriver() {
        return driver;
    }

    public static void initDriverManager() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    public static void quitDriverManager() {
        if (driver != null) {
            driver.quit();
        }
    }


}
