package com.hackathonproject.hooks;

import com.hackathonproject.base.BaseTest;
import com.hackathonproject.util.ScreenshotUtil;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CucumberHooks {

    private static final Logger log = LogManager.getLogger(CucumberHooks.class);

    @BeforeAll
    public static void beforeAll() {
        log.info("========== TEST EXECUTION STARTED ==========");
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        log.info("---------- SCENARIO STARTED: {} ----------", scenario.getName());
        log.info("Tags: {}", scenario.getSourceTagNames());
    }

    @After
    public void afterScenario(Scenario scenario) {
        log.info("Scenario Status: {}", scenario.getStatus());

        if (scenario.isFailed()) {
            log.error("SCENARIO FAILED: {}", scenario.getName());
            try {
                if (BaseTest.getDriver() != null) {
                    byte[] screenshot = ScreenshotUtil.takeScreenshotAsBytes(BaseTest.getDriver());
                    scenario.attach(screenshot, "image/png", "Failed_" + scenario.getName());
                    log.info("Failure screenshot attached to report");
                }
            } catch (Exception e) {
                log.error("Could not attach screenshot: {}", e.getMessage());
            }
        }

        log.info("---------- SCENARIO ENDED: {} ----------", scenario.getName());
    }

    @AfterAll
    public static void afterAll() {
        log.info("========== TEST EXECUTION COMPLETED ==========");
    }
}