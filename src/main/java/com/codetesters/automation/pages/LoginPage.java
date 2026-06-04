package com.codetesters.automation.pages;

import com.codetesters.automation.base.BaseTest;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * LoginPage — Page Object Model for the Login screen.
 *
 * ┌─────────────────────────────────────────────────────────┐
 * │  HACKATHON DAY CHECKLIST — do these 3 steps only:       │
 * │  1. Open browser → Inspect login page (F12)             │
 * │  2. Find username, password, button IDs/names           │
 * │  3. Replace the @FindBy locators below with real ones   │
 * └─────────────────────────────────────────────────────────┘
 *
 * Pattern    : Page Object Model (POM) with PageFactory
 * Curriculum : Day 10 (POM), Day 8 (Locators), Day 9 (Waits)
 *
 * Team: CODETESTERS | HCL Automation Hackathon
 */
public class LoginPage extends BaseTest {

    // ── STEP 1: Replace locators with your actual app values ──────────────────
    // Common locator types: @FindBy(id=""), @FindBy(name=""), @FindBy(css=""), @FindBy(xpath="")

    @FindBy(id = "username")            // 🔧 CHANGE THIS on hackathon day
    private WebElement usernameField;

    @FindBy(id = "password")            // 🔧 CHANGE THIS on hackathon day
    private WebElement passwordField;

    @FindBy(id = "loginBtn")            // 🔧 CHANGE THIS on hackathon day
    private WebElement loginButton;

    @FindBy(css = ".error-msg")         // 🔧 CHANGE THIS - shown after wrong login
    private WebElement errorMessage;

    @FindBy(css = ".welcome-header")    // 🔧 CHANGE THIS - shown after successful login
    private WebElement welcomeElement;

    // ── STEP 2: Constructor ───────────────────────────────────────────────────

    public LoginPage() {
        PageFactory.initElements(getDriver(), this);
    }

    // ── STEP 3: Page Actions (Business-level methods) ─────────────────────────

    /**
     * Opens the application URL from config.properties
     */
    public void navigateToApp() {
        String url = getConfig("app.url");
        getDriver().get(url);
        System.out.println("[LoginPage] Navigated to: " + url);
    }

    /**
     * Performs login with given credentials.
     */
    public void login(String username, String password) {
        getWait().until(ExpectedConditions.visibilityOf(usernameField));
        usernameField.clear();
        usernameField.sendKeys(username);

        passwordField.clear();
        passwordField.sendKeys(password);

        loginButton.click();
        System.out.println("[LoginPage] Login attempted → User: " + username);
    }

    /**
     * Login using credentials from config.properties (valid user)
     */
    public void loginWithValidCredentials() {
        login(getConfig("valid.username"), getConfig("valid.password"));
    }

    /**
     * Login using invalid credentials from config.properties
     */
    public void loginWithInvalidCredentials() {
        login(getConfig("invalid.username"), getConfig("invalid.password"));
    }

    /**
     * Returns true if login succeeded (welcome element is visible).
     */
    public boolean isLoginSuccessful() {
        try {
            getWait().until(ExpectedConditions.visibilityOf(welcomeElement));
            return welcomeElement.isDisplayed();
        } catch (Exception e) {
            System.out.println("[LoginPage] Login NOT successful.");
            return false;
        }
    }

    /**
     * Returns the error message text after a failed login.
     */
    public String getErrorMessage() {
        getWait().until(ExpectedConditions.visibilityOf(errorMessage));
        String msg = errorMessage.getText().trim();
        System.out.println("[LoginPage] Error message: " + msg);
        return msg;
    }

    /**
     * Returns the page title from the browser.
     */
    public String getPageTitle() {
        return getDriver().getTitle();
    }
}
