package com.codetesters.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AmazonHomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ===== LOCATORS =====

    @FindBy(id = "twotabsearchtextbox")
    private WebElement searchBox;

    @FindBy(id = "nav-search-submit-button")
    private WebElement searchButton;

    // ===== CONSTRUCTOR =====

    public AmazonHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // ===== METHODS =====

    /**
     * Opens Amazon India homepage
     */
    public void openAmazon() {
        driver.get("https://www.amazon.in");
        wait.until(ExpectedConditions.visibilityOf(searchBox));
        System.out.println("[" + getTimestamp() + "] 🌐 Amazon homepage opened successfully");
    }

    /**
     * Searches for a product on Amazon
     * @param productName - the product to search for
     */
    public void searchProduct(String productName) {
        wait.until(ExpectedConditions.elementToBeClickable(searchBox));
        searchBox.clear();
        searchBox.sendKeys(productName);

        wait.until(ExpectedConditions.elementToBeClickable(searchButton));
        searchButton.click();

        // Wait for results to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("[" + getTimestamp() + "] 🔍 Searched for: " + productName);
    }

    /**
     * Gets the current page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    private String getTimestamp() {
        return java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
