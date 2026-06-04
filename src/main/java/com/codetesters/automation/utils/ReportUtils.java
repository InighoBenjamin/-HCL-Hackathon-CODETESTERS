package com.codetesters.automation.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.codetesters.automation.base.BaseTest;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ReportUtils — Generates a professional dark-theme HTML report using ExtentReports.
 *
 * Report saved to: test-output/CODETESTERS_Report_<timestamp>.html
 * Screenshots:     test-output/screenshots/
 *
 * Called automatically by TestListener — you do NOT call this directly in tests.
 *
 * Curriculum: Day 11-12 (TestNG Listeners, Reporting)
 * Team: CODETESTERS | HCL Automation Hackathon
 */
public class ReportUtils {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    public static void initReport() {
        String timestamp  = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportPath = System.getProperty("user.dir")
                          + "/test-output/CODETESTERS_Report_" + timestamp + ".html";

        // Create directories if they don't exist
        new File(System.getProperty("user.dir") + "/test-output/screenshots/").mkdirs();

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle("CODETESTERS — HCL Automation Hackathon");
        sparkReporter.config().setReportName("Test Execution Report");
        sparkReporter.config().setTimeStampFormat("dd-MM-yyyy HH:mm:ss");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Team",        "CODETESTERS");
        extent.setSystemInfo("Event",       "HCL Automation Hackathon");
        extent.setSystemInfo("Browser",     "Chrome");
        extent.setSystemInfo("Environment", "Test");
        extent.setSystemInfo("Framework",   "Selenium + TestNG + POM");

        System.out.println("[ReportUtils] ✅ Report will be saved to: " + reportPath);
    }

    public static void createTest(String testName) {
        ExtentTest test = extent.createTest(testName);
        testThread.set(test);
    }

    public static ExtentTest getTest() {
        return testThread.get();
    }

    public static void logPass(String message) {
        getTest().log(Status.PASS, "✅ " + message);
    }

    public static void logFail(String message) {
        String screenshotPath = captureScreenshot("FAIL_" + System.currentTimeMillis());
        getTest().log(Status.FAIL, "❌ " + message)
                 .addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
    }

    public static void logInfo(String message) {
        getTest().log(Status.INFO, "ℹ️ " + message);
    }

    public static String captureScreenshot(String name) {
        String path = System.getProperty("user.dir") + "/test-output/screenshots/" + name + ".png";
        try {
            File src = ((TakesScreenshot) BaseTest.getDriver()).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File(path));
        } catch (IOException e) {
            System.err.println("[ReportUtils] Screenshot failed: " + e.getMessage());
        }
        return path;
    }

    public static void flushReport() {
        if (extent != null) {
            extent.flush();
            System.out.println("[ReportUtils] ✅ Report written to test-output/");
        }
    }
}
