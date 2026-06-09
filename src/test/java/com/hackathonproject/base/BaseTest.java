package com.hackathonproject.base;

import com.hackathonproject.runner.TestRunner;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.hackathonproject.util.ConfigReader;

import java.time.Duration;
public class BaseTest {

    private static final Logger log = LogManager.getLogger(BaseTest.class);
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driver.get();
    }

    @Before
    public void setUp() {
        String browser = TestRunner.getBrowserName() != null
                ? TestRunner.getBrowserName()
                : ConfigReader.get("browser");

        log.info("Initializing Browser: {} on thread {}", browser, Thread.currentThread().getId());

        WebDriver d;
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions c = new ChromeOptions();
                c.addArguments("--start-maximized");
                d = new ChromeDriver(c);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions e = new EdgeOptions();
                e.addArguments("--start-maximized");
                d = new EdgeDriver(e);
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions f = new FirefoxOptions();
                f.addArguments("--start-maximized");
                d = new FirefoxDriver(f);
                break;
            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }

        d.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getInt("implicit.wait")));
        d.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getInt("page.load.timeout")));
        d.get(ConfigReader.get("base.url"));

        driver.set(d);   // ← store in ThreadLocal
        log.info("Browser launched successfully");
    }

    @After
    public void tearDown() {
        if (driver.get() != null) {
            log.info("Closing browser on thread {}", Thread.currentThread().getId());
            driver.get().quit();
            driver.remove();   // ← clean up ThreadLocal
        }
    }
}