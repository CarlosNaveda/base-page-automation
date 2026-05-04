package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@CucumberOptions(
        features = "src/test/resources/features" ,
        glue = {"steps","hooks"},
        plugin = {
                "pretty",
                "html:build/cucumber-report.html"
        },
        tags = "@pagination"
)

public class TestRunner extends AbstractTestNGCucumberTests{

    @Test
    public void runTestNGCucumberTests(){

    }
}
