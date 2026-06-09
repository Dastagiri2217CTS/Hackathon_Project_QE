package com.hackathonproject.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class CorporateWellnessPage {

    WebDriver driver;

    private final By name       = By.id("name");
    private final By orgName    = By.id("organizationName");
    private final By contact    = By.id("contactNumber");
    private final By email      = By.id("officialEmailId");
    private final By orgSize    = By.id("organizationSize");
    private final By interest   = By.id("interestedIn");
    private final By submitBtn  = By.xpath("//header//button[@type='submit']");

    public CorporateWellnessPage(WebDriver driver) {
        this.driver = driver;
    }

    public void fillForm(String uName, String uOrg, String uPhone, String uEmail) {
        driver.findElement(name).sendKeys(uName);
        driver.findElement(orgName).sendKeys(uOrg);
        driver.findElement(contact).sendKeys(uPhone);
        driver.findElement(email).sendKeys(uEmail);
        new Select(driver.findElement(orgSize)).selectByVisibleText("<500");
        new Select(driver.findElement(interest)).selectByVisibleText("Taking a demo");
    }

    public boolean isSubmitEnabled() {
        return driver.findElement(submitBtn).isEnabled();
    }

    public void clickSubmit() {
        if (isSubmitEnabled()) {
            driver.findElement(submitBtn).click();
        }
    }

    /**
     * Captures HTML5 validation messages from all form fields.
     * Returns list of warning messages found.
     * Empty list means no warnings — form accepted invalid input.
     */
    public List<String> captureWarnings() {
        List<String> warnings = new ArrayList<>();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        By[] fields = { name, orgName, contact, email, orgSize, interest };
        String[] fieldNames = { "Name", "Organization", "Contact", "Email", "Org Size", "Interested In" };

        for (int i = 0; i < fields.length; i++) {
            try {
                WebElement el = driver.findElement(fields[i]);
                String msg = (String) js.executeScript(
                        "return arguments[0].validationMessage;", el);
                if (msg != null && !msg.isEmpty()) {
                    warnings.add("[" + fieldNames[i] + "] " + msg);
                }
            } catch (Exception ignored) {}
        }

        return warnings;
    }
}