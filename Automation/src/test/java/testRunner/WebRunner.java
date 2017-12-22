package testRunner;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/java/Feature", 
		glue = "webTeststepDefinition",
		monochrome = true, 
		plugin = {"pretty", 
        "html:target/cucumber/html_report"}
        )

public class WebRunner  {
    
}


