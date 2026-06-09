# 🏥 MediTesters - Practo Hackathon Automation Project

A **BDD Selenium Automation Framework** built for the Practo website using **Cucumber + TestNG + Java**, with cross-browser execution, extent reports, allure reports and Excel data writing.

---

## Team
**MediTesters** | Cognizant Hackathon Project | Dastagiri, Likith, Akshita, Tasneem, Sriya

---

## Project Overview

Automated 3 real-world test scenarios on [Practo.com](https://www.practo.com):

| # | Scenario | Description |
|---|----------|-------------|
| 1 | **Find Hospitals** | Search Bangalore hospitals that are Open 24x7, have Parking, and rating > 3.5 |
| 2 | **Diagnostic Cities** | Extract top cities from Practo Diagnostics page and store in a list |
| 3 | **Corporate Wellness** | Fill form with invalid details and capture warning/validation message |

---

## Tooling Version

| Tool          | Version |
|---------------|---------|
| Java          | 21      |
| Selenium      | 4.43.0  |
| Cucumber      | 7.34.3  |
| TestNG        | 7.12.0  |
| Maven         | 3.x     |
| Log4j2        | 2.26.0  |
| ExtentReports | 5.1.2   |
| Allure        | 2.34.0  |
| Apache POI    | 5.5.1   |

---

## 📁 Project Structure

```
src/
└── test/
    ├── java/
    │   └── com/hackathonproject/
    │       ├── base/
    │       │   └── BaseTest.java          # Driver setup with ThreadLocal
    │       ├── hooks/
    │       │   └── CucumberHooks.java     # Before/After scenario hooks
    │       ├── listeners/
    │       │   └── TestListener.java      # TestNG listener
    │       ├── pages/
    │       │   ├── HomePage.java
    │       │   ├── HospitalListingPage.java
    │       │   ├── DiagnosticPage.java
    │       │   └── CorporateWellnessPage.java
    │       ├── runner/
    │       │   └── TestRunner.java        # Cucumber + TestNG runner
    │       ├── steps/
    │       │   └── StepDefs.java          # Step definitions
    │       └── util/
    │           ├── ConfigReader.java      # Reads config.properties
    │           ├── ExcelWriter.java       # Writes results to Excel
    │           └── ScreenshotUtil.java    # Screenshot capture
    └── resources/
        ├── config/
        │   └── config.properties          # Browser, URL, wait configs
        ├── features/
        │   └── Practo.feature             # BDD feature file
        ├── allure.properties
        ├── extent.properties
        └── log4j2.xml
```

---

## Configuration

**`src/test/resources/config/config.properties`**
```properties
browser=chrome
base.url=https://www.practo.com/
implicit.wait=10
explicit.wait=15
page.load.timeout=30
screenshot.path=target/screenshots/
```

---

## How to Run

### Prerequisites
- Java
- Maven
- Chrome and/or Edge browser installed

### Run all tests
```bash
mvn clean test
```

### Run on specific browser
Tests run on **Chrome and Edge in parallel** as configured in `testng.xml`.

To run only Chrome, comment out the Edge test block in `testng.xml`.

---

## 📊 Reports

### Extent Report
After `mvn clean test`, open:
```
target/extent-report/ExtentReport.html
```

### Cucumber HTML Report
After `mvn clean test`, open:
```
target/cucumber-reports/cucumber_chrome.html
target/cucumber-reports/cucumber_edge.html
```

### Allure Report
```bash
mvn io.qameta.allure:allure-maven:report
mvn io.qameta.allure:allure-maven:serve
```
Opens automatically in browser.

---

## Cross Browser Execution

Configured in `testng.xml`:

```xml
<suite name="Practo Cross-Browser Suite" parallel="tests" thread-count="3">
    <test name="Edge Test">
        <parameter name="browser" value="edge"/>
        ...
    </test>
    <test name="Chrome Test">
        <parameter name="browser" value="chrome"/>
        ...
    </test>
    <!-- Firefox - uncomment when available
    <test name="Firefox Test">
        <parameter name="browser" value="firefox"/>
        ...
    </test>-->
</suite>
```

---

## 📝 Feature File

```gherkin
Feature: Finding Hospitals on Practo website

  Background:
    Given the user is on the Practo website home page
    
  @Smoke @Search
  Scenario: Find Hospitals in Bangalore open 24x7 with parking and rating above 3.5
    When the user searches for "Bangalore" location and "Hospital" service
    Then the hospitals with parking and rating above 3.5 are displayed

  @Smoke @Navigation
  Scenario: Capture top cities from Diagnostics page on Practo website
    When the user clicks on Lab Tests
    Then the top diagnostic cities are displayed

  @Smoke @FormValidation
  Scenario: Verify submit button is disabled when Corporate Wellness form has invalid phone number
    When the user navigates to Corporate Wellness page
    And the user fills the form with name "Gopal" organization "Cognizant" phone "8970657" email "gopal@cognizant.com"
    Then the submit button should be disabled
```

---

## Output Files

| File            | Location                                  | Description                                                       |
|-----------------|-------------------------------------------|-------------------------------------------------------------------|
| Excel Report    | `target/generatedData_*.xlsx`             | Hospital names and city list                                      |
| Screenshots     | `target/screenshots/`                     | Browser screenshots per scenario                                  |
| Extent Report   | `target/extent-report/ExtentReport.html`  | Graphical test report                                             |
| Cucumber Report | `target/cucumber-reports/cucumber_*.html` | BDD test report                                                   |
| Allure Results  | `target/allure-results/`                  | Raw allure data                                                   |

---

## Notes
- CDP version warning for Edge/Chrome is harmless and does not affect test execution
- Drivers are missing from git repository be sure to download it and place it in /src/test/resources/drivers/