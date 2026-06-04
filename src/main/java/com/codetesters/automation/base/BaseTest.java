package com.codetesters.automation.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

/**
 * BaseTest — Foundation class for all test classes.
 *
 * Responsibilities:
 *  - Launch and quit the browser before/after each test
 *  - Load config.properties (URL, credentials, timeouts)
 *  - Provide shared driver & wait to all test/page classes
 *
 * Pattern : Template Method (TestNG @BeforeMethod / @AfterMethod)
 * Curriculum: Day 7 (WebDriver Setup), Day 11 (TestNG Annotations)
 *
 * Team: CODETESTERS | HCL Automation Hackathon
 */
public class BaseTest {

    // ThreadLocal = one driver per thread (safe for parallel runs)
    private static ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();
    private static ThreadLocal<WebDriverWait> waitThread  = new ThreadLocal<>();

    // Shared config properties
    protected static Properties config = new Properties();

    static {
        // Load config.properties once when the class is first used
        try (FileInputStream fis = new FileInputStream(
                "src/test/resources/config.properties")) {
            config.load(fis);
            System.out.println("[BaseTest] config.properties loaded successfully.");
        } catch (IOException e) {
            throw new RuntimeException("[BaseTest] ERROR: config.properties not found! " + e.getMessage());
        }
    }

    // ── Getters ────────────────────────────────────────────────────────────────

    public static WebDriver getDriver() {
        return driverThread.get();
    }

    public static WebDriverWait getWait() {
        return waitThread.get();
    }

    public static String getConfig(String key) {
        return config.getProperty(key);
    }

    // ── TestNG Hooks ──────────────────────────────────────────────────────────

    @BeforeMethod
    public void setUp() {
        int implicitWait  = Integer.parseInt(config.getProperty("implicit.wait", "10"));
        int explicitWait  = Integer.parseInt(config.getProperty("explicit.wait", "20"));
        int pageLoadWait  = Integer.parseInt(config.getProperty("page.load.timeout", "30"));

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-infobars");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadWait));
        driver.manage().window().maximize();

        driverThread.set(driver);
        waitThread.set(new WebDriverWait(driver, Duration.ofSeconds(explicitWait)));

        System.out.println("[BaseTest] ✅ Browser launched successfully.");
    }

    @AfterMethod
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driverThread.remove();
            waitThread.remove();
            System.out.println("[BaseTest] ✅ Browser closed successfully.");
        }
    }
}
