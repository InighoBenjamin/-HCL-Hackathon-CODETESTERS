package com.codetesters.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResultsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ===== LOCATORS =====

    @FindBy(css = "div[data-component-type='s-search-result']")
    private List<WebElement> productCards;

    // ===== CONSTRUCTOR =====

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // ===== METHODS =====

    /**
     * Checks if a product card has a valid price (is available)
     */
    public boolean isProductAvailable(WebElement card) {
        try {
            // Try multiple price selectors
            List<WebElement> priceElements = card.findElements(By.cssSelector("span.a-price:not(.a-text-price) span.a-offscreen"));
            if (!priceElements.isEmpty()) {
                String text = priceElements.get(0).getAttribute("textContent").trim();
                return !text.isEmpty();
            }

            // Fallback: check for a-price-whole
            priceElements = card.findElements(By.cssSelector("span.a-price-whole"));
            if (!priceElements.isEmpty()) {
                String text = priceElements.get(0).getAttribute("textContent").trim();
                return !text.isEmpty();
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the product name from a product card
     */
    public String getProductName(WebElement card) {
        try {
            // Primary: Amazon uses a-text-normal class for product titles
            List<WebElement> titleElements = card.findElements(By.cssSelector("h2 a span.a-text-normal"));
            if (!titleElements.isEmpty()) {
                String text = titleElements.get(0).getAttribute("textContent").trim();
                if (!text.isEmpty()) return text;
            }

            // Fallback 1: any span inside h2 a
            titleElements = card.findElements(By.cssSelector("h2 a span"));
            for (WebElement el : titleElements) {
                String text = el.getAttribute("textContent").trim();
                if (!text.isEmpty() && text.length() > 5) return text;
            }

            // Fallback 2: h2 tag text itself
            titleElements = card.findElements(By.cssSelector("h2"));
            if (!titleElements.isEmpty()) {
                String text = titleElements.get(0).getAttribute("textContent").trim();
                if (!text.isEmpty()) return text;
            }

            return "Unknown Product";
        } catch (Exception e) {
            return "Unknown Product";
        }
    }

    /**
     * Gets the product price from a product card
     */
    public String getProductPrice(WebElement card) {
        try {
            // Primary: a-offscreen contains full price like "₹15,999"
            List<WebElement> priceElements = card.findElements(By.cssSelector("span.a-price:not(.a-text-price) span.a-offscreen"));
            if (!priceElements.isEmpty()) {
                String priceText = priceElements.get(0).getAttribute("textContent").trim();
                if (!priceText.isEmpty()) {
                    // Clean up the price text — keep ₹ and numbers
                    priceText = priceText.replaceAll("[^₹0-9,.]", "").trim();
                    // Remove trailing decimals like ".00"
                    priceText = priceText.replaceAll("\\.\\d{2}$", "");
                    if (!priceText.isEmpty()) return priceText;
                }
            }

            // Fallback: construct from a-price-whole
            List<WebElement> wholeElements = card.findElements(By.cssSelector("span.a-price-whole"));
            if (!wholeElements.isEmpty()) {
                String whole = wholeElements.get(0).getAttribute("textContent").trim();
                whole = whole.replace(".", "").trim();
                if (!whole.isEmpty()) return "₹" + whole;
            }

            return "N/A";
        } catch (Exception e) {
            return "N/A";
        }
    }

    /**
     * Captures data for the first 5 available products
     */
    public List<Map<String, String>> captureFirst5Products() {
        List<Map<String, String>> products = new ArrayList<>();
        int captured = 0;
        int index = 0;

        // Wait for product cards to load
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.cssSelector("div[data-component-type='s-search-result']")));

        // Re-fetch fresh list of product cards
        List<WebElement> cards = driver.findElements(
                By.cssSelector("div[data-component-type='s-search-result']"));

        System.out.println("[" + getTimestamp() + "] 📦 Total product cards found: " + cards.size());

        for (WebElement card : cards) {
            if (captured >= 5) {
                break;
            }

            index++;

            // Check if product is available (has a price)
            if (isProductAvailable(card)) {
                String name = getProductName(card);
                String price = getProductPrice(card);

                // Debug logging
                System.out.println("[" + getTimestamp() + "]    DEBUG — Card " + index + " → Name: [" + name + "], Price: [" + price + "]");

                // Skip if we couldn't get valid data
                if (name.equals("Unknown Product") || price.equals("N/A") || name.isEmpty() || price.isEmpty()) {
                    System.out.println("[" + getTimestamp() + "] ⏭️ Product " + index + " skipped — invalid data");
                    continue;
                }

                // Truncate very long names for clean Excel output
                if (name.length() > 120) {
                    name = name.substring(0, 120) + "...";
                }

                Map<String, String> product = new HashMap<>();
                product.put("name", name);
                product.put("price", price);
                products.add(product);

                captured++;
                System.out.println("[" + getTimestamp() + "] 📦 Product " + captured + ": " + name);
                System.out.println("[" + getTimestamp() + "]    💰 Price: " + price);
            } else {
                System.out.println("[" + getTimestamp() + "] ⏭️ Product " + index + " unavailable, skipping");
            }
        }

        System.out.println("[" + getTimestamp() + "] ✅ Captured " + captured + " products successfully!");
        return products;
    }

    /**
     * Gets the total number of product cards on the page
     */
    public int getProductCount() {
        return productCards.size();
    }

    /**
     * Checks if search results are displayed
     */
    public boolean areResultsDisplayed() {
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                    By.cssSelector("div[data-component-type='s-search-result']")));
            return !productCards.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private String getTimestamp() {
        return java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
