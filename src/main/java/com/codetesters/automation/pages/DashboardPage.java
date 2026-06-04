package com.codetesters.automation.pages;

import com.codetesters.automation.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * DashboardPage — Page Object Model for the Main Workflow screen.
 *
 * Covers all common hackathon scenarios:
 *  ✅ Search / Filter
 *  ✅ Dropdowns (static & dynamic)
 *  ✅ Add to cart / Submit transaction
 *  ✅ Alert / Popup handling
 *  ✅ Iframe switching
 *  ✅ New window/tab handling
 *  ✅ Table data reading
 *  ✅ JavaScript scroll / click
 *
 * ┌─────────────────────────────────────────────────────────┐
 * │  HACKATHON DAY: Only replace @FindBy locators below.    │
 * │  All methods (search, cart, alerts) stay the same!      │
 * └─────────────────────────────────────────────────────────┘
 *
 * Pattern    : Page Object Model (POM) with PageFactory
 * Curriculum : Day 8 (Locators), Day 9 (Dynamic Elements), Day 10 (POM)
 *
 * Team: CODETESTERS | HCL Automation Hackathon
 */
public class DashboardPage extends BaseTest {

    // ── LOCATORS — Replace these on Hackathon Day ─────────────────────────────

    @FindBy(id = "searchBox")
    private WebElement searchBox;

    @FindBy(id = "searchBtn")
    private WebElement searchButton;

    @FindBy(css = ".item-list .item")
    private List<WebElement> itemList;

    @FindBy(id = "categoryDropdown")
    private WebElement categoryDropdown;

    @FindBy(css = ".submit-btn")
    private WebElement submitButton;

    @FindBy(css = ".success-banner, .confirmation-msg, #successMsg")
    private WebElement successMessage;

    @FindBy(css = ".cart-count, #cartBadge")
    private WebElement cartCount;

    @FindBy(id = "logoutBtn")
    private WebElement logoutButton;

    // ── Constructor ───────────────────────────────────────────────────────────

    public DashboardPage() {
        PageFactory.initElements(getDriver(), this);
    }

    // ── Search ────────────────────────────────────────────────────────────────

    public void searchFor(String keyword) {
        getWait().until(ExpectedConditions.elementToBeClickable(searchBox));
        searchBox.clear();
        searchBox.sendKeys(keyword);
        searchButton.click();
        System.out.println("[DashboardPage] Searched: " + keyword);
    }

    public int getResultCount() {
        getWait().until(ExpectedConditions.visibilityOfAllElements(itemList));
        return itemList.size();
    }

    // ── Dropdown ──────────────────────────────────────────────────────────────

    /**
     * Selects from a standard HTML <select> dropdown by visible text.
     * Curriculum: Day 8 (Selenium Select class)
     */
    public void selectFromDropdown(String visibleText) {
        getWait().until(ExpectedConditions.elementToBeClickable(categoryDropdown));
        Select select = new Select(categoryDropdown);
        select.selectByVisibleText(visibleText);
        System.out.println("[DashboardPage] Dropdown selected: " + visibleText);
    }

    /**
     * Clicks a dynamic dropdown option by partial text match (XPath).
     * Use this if the dropdown is NOT a <select> tag.
     */
    public void selectFromDynamicDropdown(String optionText) {
        String xpath = "//ul[contains(@class,'dropdown')]//li[contains(text(),'" + optionText + "')]";
        WebElement option = getWait().until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        option.click();
        System.out.println("[DashboardPage] Dynamic dropdown selected: " + optionText);
    }

    // ── Submit / Transaction ──────────────────────────────────────────────────

    public void clickSubmit() {
        getWait().until(ExpectedConditions.elementToBeClickable(submitButton));
        submitButton.click();
        System.out.println("[DashboardPage] Submit clicked.");
    }

    public String getSuccessMessage() {
        getWait().until(ExpectedConditions.visibilityOf(successMessage));
        String msg = successMessage.getText().trim();
        System.out.println("[DashboardPage] Success message: " + msg);
        return msg;
    }

    public int getCartCount() {
        getWait().until(ExpectedConditions.visibilityOf(cartCount));
        return Integer.parseInt(cartCount.getText().trim());
    }

    // ── Alert / Popup Handling ────────────────────────────────────────────────

    /**
     * Accepts a JS Alert or Confirm dialog.
     * Curriculum: Day 9 (Alert Handling)
     */
    public String acceptAlert() {
        getWait().until(ExpectedConditions.alertIsPresent());
        String alertText = getDriver().switchTo().alert().getText();
        getDriver().switchTo().alert().accept();
        System.out.println("[DashboardPage] Alert accepted. Text: " + alertText);
        return alertText;
    }

    public void dismissAlert() {
        getWait().until(ExpectedConditions.alertIsPresent());
        getDriver().switchTo().alert().dismiss();
        System.out.println("[DashboardPage] Alert dismissed.");
    }

    // ── Iframe Handling ───────────────────────────────────────────────────────

    /**
     * Switches into an iframe by its ID.
     * Curriculum: Day 9 (Frame Handling)
     */
    public void switchToFrame(String frameId) {
        getWait().until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id(frameId)));
        System.out.println("[DashboardPage] Switched to iframe: " + frameId);
    }

    public void switchToFrameByIndex(int index) {
        getWait().until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(index));
        System.out.println("[DashboardPage] Switched to iframe index: " + index);
    }

    public void switchToMainContent() {
        getDriver().switchTo().defaultContent();
        System.out.println("[DashboardPage] Back to main content.");
    }

    // ── Multiple Window / Tab Handling ────────────────────────────────────────

    /**
     * Switches focus to the newest browser window/tab.
     * Curriculum: Day 9 (Window Handling)
     */
    public void switchToNewWindow() {
        String current = getDriver().getWindowHandle();
        for (String handle : getDriver().getWindowHandles()) {
            if (!handle.equals(current)) {
                getDriver().switchTo().window(handle);
                System.out.println("[DashboardPage] Switched to new window.");
                break;
            }
        }
    }

    public void closeCurrentWindowAndSwitch() {
        getDriver().close();
        String remaining = getDriver().getWindowHandles().iterator().next();
        getDriver().switchTo().window(remaining);
        System.out.println("[DashboardPage] Closed child window, back to parent.");
    }

    // ── Table Data ────────────────────────────────────────────────────────────

    /**
     * Reads a cell value from an HTML table.
     * @param tableId  - id attribute of the <table> element
     * @param row      - row index (1-based, excluding header)
     * @param col      - column index (1-based)
     */
    public String getTableCell(String tableId, int row, int col) {
        String xpath = String.format("//table[@id='%s']/tbody/tr[%d]/td[%d]", tableId, row, col);
        WebElement cell = getWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
        return cell.getText().trim();
    }

    // ── JavaScript Utilities ──────────────────────────────────────────────────

    /**
     * Scrolls to a specific element (useful when it's off-screen).
     */
    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Clicks an element via JavaScript (use when normal click() doesn't work).
     */
    public void jsClick(WebElement element) {
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", element);
    }

    // ── Logout ────────────────────────────────────────────────────────────────

    public void logout() {
        getWait().until(ExpectedConditions.elementToBeClickable(logoutButton));
        logoutButton.click();
        System.out.println("[DashboardPage] Logged out.");
    }
}
