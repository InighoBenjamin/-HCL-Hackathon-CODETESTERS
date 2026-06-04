package com.codetesters.automation.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestListener implements ITestListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setDocumentTitle("Amazon Price Tracker — Test Report");
        sparkReporter.config().setReportName("CODETESTERS — HCL Hackathon");
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setTimeStampFormat("dd-MMM-yyyy HH:mm:ss");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Team", "CODETESTERS");
        extent.setSystemInfo("Project", "Amazon Price Tracker");
        extent.setSystemInfo("Framework", "Selenium Java + TestNG + POM");
        extent.setSystemInfo("Browser", "Chrome");
        extent.setSystemInfo("Environment", "Production (amazon.in)");

        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("  CODETESTERS — Amazon Price Tracker v1.0");
        System.out.println("  Started: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(
                result.getMethod().getMethodName(),
                result.getMethod().getDescription()
        );
        test.set(extentTest);
        System.out.println("[" + getTimestamp() + "] ▶️ Starting test: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, "✅ Test PASSED: " + result.getMethod().getMethodName());
        System.out.println("[" + getTimestamp() + "] ✅ Test PASSED: " + result.getMethod().getMethodName());
        System.out.println();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().log(Status.FAIL, "❌ Test FAILED: " + result.getMethod().getMethodName());
        test.get().log(Status.FAIL, "Error: " + result.getThrowable().getMessage());
        System.out.println("[" + getTimestamp() + "] ❌ Test FAILED: " + result.getMethod().getMethodName());
        System.out.println("[" + getTimestamp() + "]    Error: " + result.getThrowable().getMessage());
        System.out.println();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().log(Status.SKIP, "⏭️ Test SKIPPED: " + result.getMethod().getMethodName());
        System.out.println("[" + getTimestamp() + "] ⏭️ Test SKIPPED: " + result.getMethod().getMethodName());
        System.out.println();
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("  📊 REPORT GENERATED");
        System.out.println("  ExtentReport: test-output/ExtentReport.html");
        System.out.println("  Excel: testdata/PriceData.xlsx");
        System.out.println("  Screenshots: screenshots/");
        System.out.println("═══════════════════════════════════════════════════════");
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void logInfo(String message) {
        if (test.get() != null) {
            test.get().log(Status.INFO, message);
        }
    }

    public static void logPass(String message) {
        if (test.get() != null) {
            test.get().log(Status.PASS, message);
        }
    }

    public static void logWarning(String message) {
        if (test.get() != null) {
            test.get().log(Status.WARNING, message);
        }
    }

    private String getTimestamp() {
        return java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
