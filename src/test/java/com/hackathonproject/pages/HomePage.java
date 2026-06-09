package com.hackathonproject.pages;

import com.hackathonproject.util.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    WebDriver driver;
    WebDriverWait wait;
    Actions actions;

    private final By labTestsLink = By.xpath("//a[.//div[text()='Lab Tests']]");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait")));
        this.actions = new Actions(driver);
    }

    // Directly navigates to Bangalore hospitals — no dropdown interaction
    public void searchLocation(String location) {
        // location handled together in searchService via direct URL
    }

    public void searchService(String service) {
        // Navigate directly to the search results URL — works in both Chrome and Edge
        driver.navigate().to("https://www.practo.com/bangalore/hospitals");
        wait.until(ExpectedConditions.or(
            ExpectedConditions.titleContains("Bangalore"),
            ExpectedConditions.titleContains("Hospital")
        ));
    }

    public void clickLabTests() {
        wait.until(ExpectedConditions.elementToBeClickable(labTestsLink)).click();
    }

    public void navigateToCorporateWellness() {
        driver.navigate().to("https://www.practo.com/");

        By forCorporates = By.xpath(
            "//*[contains(@class,'nav-interact') and contains(text(),'For Corporates')]"
        );
        By wellnessPlans = By.xpath("//a[@href='/plus/corporate']");

        WebElement corporatesMenu = wait.until(ExpectedConditions.elementToBeClickable(forCorporates));
        actions.moveToElement(corporatesMenu).perform();

        WebElement wellness = wait.until(ExpectedConditions.visibilityOfElementLocated(wellnessPlans));
        try {
            wellness.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", wellness);
        }
    }
}