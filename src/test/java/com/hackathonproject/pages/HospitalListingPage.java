package com.hackathonproject.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.hackathonproject.base.BaseTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HospitalListingPage {

    WebDriver driver;
    private static final Logger log = LogManager.getLogger(HospitalListingPage.class);
    private final By hospitalCards = By.cssSelector(".c-estb-card");

    public HospitalListingPage(WebDriver driver) {
        this.driver = driver;
    }

    public List<String> getHospitalsWithParking(double minRating) throws InterruptedException {
        List<String> qualifiedHospitals = new ArrayList<>();
        String mainHandle = driver.getWindowHandle();

        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight/4);");
        Thread.sleep(2000);

        List<WebElement> cards = driver.findElements(hospitalCards);
        log.info("Processing {} hospitals...", cards.size());

        for (WebElement card : cards) {
            try {
                String cardText = card.getText();
                boolean isHighRated = checkRating(cardText, minRating);
                boolean is24x7 = checkOpen247(cardText);

                if (isHighRated && is24x7) {
                    WebElement nameLink = card.findElement(By.cssSelector(".line-1"));
                    String hospitalName = nameLink.getText();

                    // Click and open hospital detail page
                    nameLink.click();
                    switchTab(mainHandle);

                    if (checkParkingAvailable()) {
                        log.info("Found: {}", hospitalName);
                        qualifiedHospitals.add(hospitalName);
                    }

                    // Close tab and return to listing
                    driver.close();
                    driver.switchTo().window(mainHandle);
                }

            } catch (Exception e) {
                log.warn("Skipping card due to error: {}", e.getMessage());
                if (driver.getWindowHandles().size() > 1) {
                    driver.close();
                    driver.switchTo().window(mainHandle);
                }
            }
        }

        return qualifiedHospitals;
    }

    private boolean checkRating(String text, double minRating) {
        for (String word : text.split("[\\n\\s]+")) {
            if (word.matches("[0-5]\\.[0-9]")) {
                if (Double.parseDouble(word) > minRating) return true;
            }
        }
        return false;
    }

    private boolean checkOpen247(String text) {
        text = text.toLowerCase();
        return text.contains("24/7") || text.contains("open 24") || text.contains("open 24x7");
    }

    private void switchTab(String mainHandle) {
        Set<String> handles = driver.getWindowHandles();
        for (String h : handles) {
            if (!h.equals(mainHandle)) driver.switchTo().window(h);
        }
    }

    private boolean checkParkingAvailable() {
        try {
            List<WebElement> readMore = driver.findElements(
                By.xpath("//*[text()='Read more info']"));
            if (!readMore.isEmpty()) readMore.get(0).click();

            List<WebElement> amenities = driver.findElements(
                By.xpath("//*[@data-qa-id='amenity_item']"));
            for (WebElement am : amenities) {
                if (am.getText().contains("Parking")) return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}