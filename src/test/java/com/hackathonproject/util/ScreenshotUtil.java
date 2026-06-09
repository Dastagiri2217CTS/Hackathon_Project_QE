package com.hackathonproject.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    private static final Logger log = LogManager.getLogger(ScreenshotUtil.class);

    public static void takeScreenshot(WebDriver driver, String screenshotName) throws IOException {
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File source = screenshot.getScreenshotAs(OutputType.FILE);

        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS").format(new Date());
        String browser = com.hackathonproject.runner.TestRunner.getBrowserName() != null
                ? com.hackathonproject.runner.TestRunner.getBrowserName()
                : "unknown";

        Path target = Path.of("target", "screenshots",
                browser + "_" + screenshotName + "_" + timestamp + ".png");

        target.getParent().toFile().mkdirs();
        FileHandler.copy(source, target.toFile());
        log.info("Screenshot saved: {}", target.getFileName());
    }

    public static byte[] takeScreenshotAsBytes(WebDriver driver) {
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        return screenshot.getScreenshotAs(OutputType.BYTES);
    }
}