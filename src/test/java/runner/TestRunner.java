package runner;

import driverManager.DriverManager;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterSuite;


@CucumberOptions(
        features = "src/test/resources/features" ,
        glue = {"steps","hooks"},
        plugin = {
                "pretty",
                "html:build/cucumber-report.html"
        },
        tags = "@response"
)

public class TestRunner extends AbstractTestNGCucumberTests{

    @AfterSuite
    public void tearDownSuite() {
        DriverManager.quitDriverManager();
    }

}
