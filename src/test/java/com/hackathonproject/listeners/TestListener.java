package com.hackathonproject.listeners;

import com.hackathonproject.base.BaseTest;
import com.hackathonproject.runner.TestRunner;
import com.hackathonproject.util.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        log.info("========== TEST SUITE STARTED: {} ==========", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("========== TEST SUITE FINISHED: {} ==========", context.getName());
        log.info("Passed : {}", context.getPassedTests().size());
        log.info("Failed : {}", context.getFailedTests().size());
        log.info("Skipped: {}", context.getSkippedTests().size());

        String browser = TestRunner.getBrowserName();
        if (browser != null) {
            Path dir = Path.of("target/cucumber-reports");
            try {
                Path html = dir.resolve("cucumber.html");
                Path json = dir.resolve("cucumber.json");
                if (Files.exists(html)) {
                    Files.copy(html, dir.resolve("cucumber_" + browser + ".html"),
                            StandardCopyOption.REPLACE_EXISTING);
                }
                if (Files.exists(json)) {
                    Files.copy(json, dir.resolve("cucumber_" + browser + ".json"),
                            StandardCopyOption.REPLACE_EXISTING);
                }
                log.info("Cucumber report saved for browser: {}", browser);
            } catch (IOException e) {
                log.warn("Could not copy Cucumber report: {}", e.getMessage());
            }
        }
    }

    @Override public void onTestStart(ITestResult result)   { log.info(">>> TEST STARTED: {}", result.getName()); }
    @Override public void onTestSuccess(ITestResult result) { log.info(" TEST PASSED: {}", result.getName()); }
    @Override public void onTestSkipped(ITestResult result) { log.warn(" TEST SKIPPED: {}", result.getName()); }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("✘ TEST FAILED: {}", result.getName());
        log.error("Reason: {}", result.getThrowable().getMessage());
        try {
            if (BaseTest.getDriver() != null) {
                ScreenshotUtil.takeScreenshot(BaseTest.getDriver(), "FAILED_" + result.getName());
            }
        } catch (IOException e) {
            log.error("Could not take failure screenshot: {}", e.getMessage());
        }
    }
}