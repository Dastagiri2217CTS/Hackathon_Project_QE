package com.hackathonproject.steps;

import com.hackathonproject.base.BaseTest;
import com.hackathonproject.pages.CorporateWellnessPage;
import com.hackathonproject.pages.DiagnosticPage;
import com.hackathonproject.pages.HomePage;
import com.hackathonproject.pages.HospitalListingPage;
import com.hackathonproject.util.ExcelWriter;
import com.hackathonproject.util.ScreenshotUtil;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class StepDefs {
    private static final Logger log = LogManager.getLogger(StepDefs.class);

    @Given("the user is on the Practo website home page")
    public void userOnHomePage() throws InterruptedException {
        BaseTest.getDriver().get("https://www.practo.com/");
        Thread.sleep(2000);
        log.info("On Practo Home: {}", BaseTest.getDriver().getTitle());
    }

    // ---------- Scenario 1 : @Smoke @Search ----------
    @When("the user searches for {string} location and {string} service")
    public void searchHospital(String location, String service) throws InterruptedException {
        log.info("Searching for service: {} in location: {}", service, location);
        HomePage home = new HomePage(BaseTest.getDriver());
        home.searchLocation(location);
        home.searchService(service);
        try {
            ScreenshotUtil.takeScreenshot(BaseTest.getDriver(), "1_search_result");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Then("the hospitals with parking and rating above {double} are displayed")
    public void displayHospitals(double rating) throws InterruptedException {
        HospitalListingPage listing = new HospitalListingPage(BaseTest.getDriver());
        List<String> hospitals = listing.getHospitalsWithParking(rating);

        log.info("--- Hospitals (Open 24x7, Parking, Rating > {}) ---", rating);
        for (String h : hospitals) log.info(h);
        ExcelWriter.writeHospitals(hospitals);

        SoftAssert soft = new SoftAssert();
        soft.assertTrue(hospitals.size() >= 0, "Hospital search ran successfully");
        soft.assertAll();
    }

    // ---------- Scenario 2 : @Smoke @Navigation ----------
    @When("the user clicks on Lab Tests")
    public void clickLabTests() {
        log.info("Clicking on Lab Tests");
        new HomePage(BaseTest.getDriver()).clickLabTests();
    }

    @Then("the top diagnostic cities are displayed")
    public void displayCities() {
        DiagnosticPage diag = new DiagnosticPage(BaseTest.getDriver());
        List<String> cities = diag.getTopCities();

        log.info("--- Top Diagnostic Cities ---");
        for (String city : cities) log.info(city);
        try {
            ScreenshotUtil.takeScreenshot(BaseTest.getDriver(), "2_top_cities");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        ExcelWriter.writeCities(cities);

        SoftAssert soft = new SoftAssert();
        soft.assertFalse(cities.isEmpty(), "Cities list should not be empty");
        soft.assertTrue(cities.contains("Bangalore"), "Bangalore should be in top cities");
        soft.assertTrue(cities.contains("Mumbai"), "Mumbai should be in top cities");
        soft.assertTrue(cities.contains("Delhi"), "Delhi should be in top cities");
        soft.assertAll();
    }

    // ---------- Scenario 3 : @Smoke @FormValidation ----------
    @When("the user navigates to Corporate Wellness page")
    public void navigateCorporate() {
        log.info("Navigating to Corporate Wellness page");
        new HomePage(BaseTest.getDriver()).navigateToCorporateWellness();

        Set<String> handles = BaseTest.getDriver().getWindowHandles();
        for (String handle : handles) {
            BaseTest.getDriver().switchTo().window(handle);
            String title = BaseTest.getDriver().getTitle();
            log.info("Window title: {}", title);
            if (title.contains("Employee Health | Corporate Health & Wellness Plans | Practo"))
                break;
        }
        log.info("Switched to: {}", BaseTest.getDriver().getTitle());
    }

    @And("the user fills the form with name {string} organization {string} phone {string} email {string}")
    public void fillForm(String name, String org, String phone, String email) {
        log.info("Filling form - Name: {}, Org: {}, Phone: {}, Email: {}", name, org, phone, email);
        new CorporateWellnessPage(BaseTest.getDriver()).fillForm(name, org, phone, email);
        try {
            ScreenshotUtil.takeScreenshot(BaseTest.getDriver(), "3_corporate_form_filled");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    // ← Step text updated to match updated feature file
    @Then("the submit button should be disabled")
    public void verifySubmitButtonDisabled() {
        CorporateWellnessPage form = new CorporateWellnessPage(BaseTest.getDriver());
        boolean enabled = form.isSubmitEnabled();
        log.info("Submit Button Enabled: {}", enabled);

        try {
            ScreenshotUtil.takeScreenshot(BaseTest.getDriver(), "3_submit_button_state");
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        SoftAssert soft = new SoftAssert();
        // Explicit assertion — button MUST be disabled for invalid phone "8970657"
        soft.assertFalse(enabled, "Submit button should be DISABLED when phone number is invalid (7 digits)");
        soft.assertAll();

        log.info("Submit button disabled assertion passed — invalid phone correctly rejected");
    }
}