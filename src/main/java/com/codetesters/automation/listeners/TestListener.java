package com.codetesters.automation.listeners;

import com.codetesters.automation.utils.ReportUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestListener — Automatically hooks into every @Test without touching test classes.
 *
 * What it does automatically:
 *  ✅ Starts the HTML report before the suite
 *  ✅ Logs PASS / FAIL / SKIP for every test
 *  ✅ Captures a screenshot on every FAILURE
 *  ✅ Writes and closes the report after the suite
 *
 * Registered in testng.xml — no code changes needed in test classes.
 *
 * Curriculum: Day 11 (TestNG Listeners — ITestListener)
 * Team: CODETESTERS | HCL Automation Hackathon
 */
public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║   CODETESTERS — HCL HACKATHON SUITE STARTED      ║");
        System.out.println("║   Suite: " + context.getName());
        System.out.println("╚══════════════════════════════════════════════════╝\n");
        ReportUtils.initReport();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String name = result.getMethod().getMethodName();
        System.out.println("\n▶ [TEST STARTED] → " + name);
        ReportUtils.createTest(name);
        ReportUtils.logInfo("Test Started: " + name);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String name = result.getMethod().getMethodName();
        System.out.println("✅ [TEST PASSED] → " + name);
        ReportUtils.logPass("Test Passed Successfully: " + name);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String name  = result.getMethod().getMethodName();
        String error = result.getThrowable().getMessage();
        System.out.println("❌ [TEST FAILED] → " + name + " | " + error);
        ReportUtils.logFail("Test Failed: " + name + " | Reason: " + error);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String name = result.getMethod().getMethodName();
        System.out.println("⏭ [TEST SKIPPED] → " + name);
        ReportUtils.getTest().skip("Test Skipped: " + name);
    }

    @Override
    public void onFinish(ITestContext context) {
        ReportUtils.flushReport();
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║   SUITE FINISHED                                 ║");
        System.out.println("║   ✅ Passed  : " + context.getPassedTests().size());
        System.out.println("║   ❌ Failed  : " + context.getFailedTests().size());
        System.out.println("║   ⏭ Skipped : " + context.getSkippedTests().size());
        System.out.println("╚══════════════════════════════════════════════════╝\n");
    }
}
