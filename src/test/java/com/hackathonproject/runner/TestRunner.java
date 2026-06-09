package com.hackathonproject.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.hackathonproject.util.ExcelWriter;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.hackathonproject.steps", "com.hackathonproject.base", "com.hackathonproject.hooks"},
        tags = "@Smoke",
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        }
)
public class TestRunner extends AbstractTestNGCucumberTests {

    private static String browserName;

    public static String getBrowserName() {
        return browserName;
    }

    @BeforeClass
    @Parameters("browser")
    public void setBrowser(@Optional("chrome") String browser) {
        browserName = browser;
    }

    @AfterClass
    public void closeBrowser() {
        ExcelWriter.cleanup();
    }
}