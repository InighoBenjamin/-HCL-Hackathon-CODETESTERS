package com.codetesters.automation.tests;

import com.codetesters.automation.base.BaseTest;
import com.codetesters.automation.pages.DashboardPage;
import com.codetesters.automation.pages.LoginPage;
import com.codetesters.automation.utils.ExcelUtils;
import com.codetesters.automation.utils.ReportUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * WorkflowTest — End-to-End test for the main use case workflow.
 *
 * ┌─────────────────────────────────────────────────────────────┐
 * │  HACKATHON DAY: This is your MAIN test class.               │
 * │  Replace the method bodies to match the actual use case.    │
 * │  The structure (login → action → verify → logout) stays!   │
 * └─────────────────────────────────────────────────────────────┘
 *
 * Test Cases Covered:
 *  TC_04 : End-to-End workflow (login → search → submit → verify → logout)
 *  TC_05 : Data-Driven workflow using Excel
 *  TC_06 : Alert handling verification
 *  TC_07 : Dropdown selection verification
 *
 * Curriculum: Day 10 (POM), Day 9 (Dynamic Elements), Day 11 (TestNG), Day 12 (DDT)
 * Team: CODETESTERS | HCL Automation Hackathon
 */
public class WorkflowTest extends BaseTest {

    // ── TC_04: Full End-to-End Workflow ───────────────────────────────────────

    @Test(priority = 1,
          description = "TC_04 - End-to-End: Login → Perform Action → Verify → Logout")
    public void testEndToEndWorkflow() {
        ReportUtils.logInfo("TC_04: Starting End-to-End workflow test");

        // Step 1: Login
        LoginPage loginPage = new LoginPage();
        loginPage.navigateToApp();
        loginPage.loginWithValidCredentials();
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login failed — cannot proceed.");
        ReportUtils.logPass("Step 1: Login successful ✅");

        // Step 2: Perform main workflow action
        DashboardPage dashboard = new DashboardPage();

        // 🔧 HACKATHON DAY: Replace "Laptop" with the actual search/action item
        dashboard.searchFor("Laptop");
        ReportUtils.logInfo("Step 2: Searched for item");

        // 🔧 HACKATHON DAY: Replace "Electronics" with actual dropdown value
        dashboard.selectFromDropdown("Electronics");
        ReportUtils.logInfo("Step 3: Dropdown selected");

        // Step 3: Submit / Trigger the main action
        dashboard.clickSubmit();
        ReportUtils.logInfo("Step 4: Submit clicked");

        // Step 4: Verify success
        String successMsg = dashboard.getSuccessMessage();
        Assert.assertFalse(successMsg.isEmpty(), "Success message not displayed after action.");
        ReportUtils.logPass("Step 5: Success message verified → " + successMsg + " ✅");

        // Step 5: Logout
        dashboard.logout();
        ReportUtils.logPass("TC_04 PASSED — Full workflow completed successfully ✅");
    }

    // ── TC_05: Data-Driven Workflow ───────────────────────────────────────────

    /**
     * Excel Sheet "WorkflowData" columns:
     *  | SearchTerm | Category    | ExpectedMessage     |
     *  | Laptop     | Electronics | Order placed!       |
     *  | Phone      | Mobile      | Order placed!       |
     */
    @DataProvider(name = "workflowData")
    public Object[][] getWorkflowData() {
        return ExcelUtils.getTestData("WorkflowData");
    }

    @Test(priority = 2,
          dataProvider = "workflowData",
          description = "TC_05 - Data-Driven: Run workflow with multiple Excel datasets")
    public void testDataDrivenWorkflow(String searchTerm, String category, String expectedMessage) {
        ReportUtils.logInfo("TC_05: Data-Driven Workflow → " + searchTerm + " | " + category);

        // Login
        LoginPage loginPage = new LoginPage();
        loginPage.navigateToApp();
        loginPage.loginWithValidCredentials();
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login failed.");

        // Perform workflow with Excel data
        DashboardPage dashboard = new DashboardPage();
        dashboard.searchFor(searchTerm);
        dashboard.selectFromDropdown(category);
        dashboard.clickSubmit();

        // Verify
        String actualMessage = dashboard.getSuccessMessage();
        Assert.assertTrue(actualMessage.contains(expectedMessage),
            "Expected: '" + expectedMessage + "' but got: '" + actualMessage + "'");

        ReportUtils.logPass("TC_05 PASSED → " + searchTerm + " workflow verified ✅");
        dashboard.logout();
    }

    // ── TC_06: Alert Handling ─────────────────────────────────────────────────

    @Test(priority = 3,
          description = "TC_06 - Verify alert popup handling after an action")
    public void testAlertHandling() {
        ReportUtils.logInfo("TC_06: Testing alert/popup handling");

        LoginPage loginPage = new LoginPage();
        loginPage.navigateToApp();
        loginPage.loginWithValidCredentials();

        DashboardPage dashboard = new DashboardPage();

        // Trigger action that causes alert
        dashboard.clickSubmit();

        // Handle alert
        String alertText = dashboard.acceptAlert();
        Assert.assertFalse(alertText.isEmpty(), "Alert text was empty.");
        ReportUtils.logPass("TC_06 PASSED — Alert handled. Text: " + alertText + " ✅");
    }

    // ── TC_07: Dropdown Selection ─────────────────────────────────────────────

    @Test(priority = 4,
          description = "TC_07 - Verify dropdown selection works correctly")
    public void testDropdownSelection() {
        ReportUtils.logInfo("TC_07: Testing dropdown selection");

        LoginPage loginPage = new LoginPage();
        loginPage.navigateToApp();
        loginPage.loginWithValidCredentials();

        DashboardPage dashboard = new DashboardPage();

        // 🔧 HACKATHON DAY: Replace with actual dropdown option from the app
        dashboard.selectFromDropdown("Electronics");
        ReportUtils.logInfo("Category selected: Electronics");

        // Verify results updated
        int count = dashboard.getResultCount();
        Assert.assertTrue(count > 0, "No results shown after dropdown selection.");
        ReportUtils.logPass("TC_07 PASSED — Dropdown filtered " + count + " results ✅");
    }
}
