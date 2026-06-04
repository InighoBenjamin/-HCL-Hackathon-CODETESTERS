package com.codetesters.automation.tests;

import com.codetesters.automation.base.BaseTest;
import com.codetesters.automation.pages.LoginPage;
import com.codetesters.automation.utils.ExcelUtils;
import com.codetesters.automation.utils.ReportUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * LoginTest — Verifies the Login functionality of the application.
 *
 * Test Cases Covered:
 *  TC_01 : Valid login should navigate to dashboard
 *  TC_02 : Invalid login should show error message
 *  TC_03 : Data-Driven login using Excel (multiple credentials)
 *
 * Curriculum: Day 11 (TestNG @Test, @DataProvider), Day 12 (Data-Driven Testing)
 * Team: CODETESTERS | HCL Automation Hackathon
 */
public class LoginTest extends BaseTest {

    // ── TC_01: Valid Login ────────────────────────────────────────────────────

    @Test(priority = 1,
          description = "TC_01 - Valid credentials should login successfully")
    public void testValidLogin() {
        ReportUtils.logInfo("TC_01: Testing valid login flow");

        LoginPage loginPage = new LoginPage();
        loginPage.navigateToApp();
        ReportUtils.logInfo("Navigated to: " + getConfig("app.url"));

        loginPage.loginWithValidCredentials();
        ReportUtils.logInfo("Login attempted with valid credentials");

        boolean isLoggedIn = loginPage.isLoginSuccessful();
        ReportUtils.logInfo("Login successful: " + isLoggedIn);

        Assert.assertTrue(isLoggedIn,
            "❌ Login FAILED — Welcome element not visible after valid login.");

        ReportUtils.logPass("TC_01 PASSED — Valid login successful ✅");
    }

    // ── TC_02: Invalid Login ──────────────────────────────────────────────────

    @Test(priority = 2,
          description = "TC_02 - Invalid credentials should show error message")
    public void testInvalidLogin() {
        ReportUtils.logInfo("TC_02: Testing invalid login flow");

        LoginPage loginPage = new LoginPage();
        loginPage.navigateToApp();

        loginPage.loginWithInvalidCredentials();
        ReportUtils.logInfo("Login attempted with invalid credentials");

        String errorMsg = loginPage.getErrorMessage();
        ReportUtils.logInfo("Error message displayed: " + errorMsg);

        Assert.assertFalse(errorMsg.isEmpty(),
            "❌ Error message NOT displayed after invalid login.");

        ReportUtils.logPass("TC_02 PASSED — Error message shown: " + errorMsg);
    }

    // ── TC_03: Data-Driven Login (Excel) ──────────────────────────────────────

    /**
     * @DataProvider reads credentials from Excel sheet "LoginData".
     *
     * Excel Sheet "LoginData" columns:
     *  | Username | Password | ExpectedResult |
     *  | admin    | pass123  | success        |
     *  | wrong    | wrong    | failure        |
     */
    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return ExcelUtils.getTestData("LoginData");
    }

    @Test(priority = 3,
          dataProvider = "loginData",
          description = "TC_03 - Data-Driven login with multiple Excel credentials")
    public void testDataDrivenLogin(String username, String password, String expectedResult) {
        ReportUtils.logInfo("TC_03: Data-Driven Test → User: " + username + " | Expected: " + expectedResult);

        LoginPage loginPage = new LoginPage();
        loginPage.navigateToApp();
        loginPage.login(username, password);

        if (expectedResult.equalsIgnoreCase("success")) {
            Assert.assertTrue(loginPage.isLoginSuccessful(),
                "Expected successful login for user: " + username);
            ReportUtils.logPass("TC_03 PASSED — Login successful for: " + username);
        } else {
            String error = loginPage.getErrorMessage();
            Assert.assertFalse(error.isEmpty(),
                "Expected error message for invalid user: " + username);
            ReportUtils.logPass("TC_03 PASSED — Error shown for invalid user: " + username);
        }
    }
}
