package com.codetesters.automation.tests;

import com.codetesters.automation.base.BaseTest;
import com.codetesters.automation.listeners.TestListener;
import com.codetesters.automation.pages.AmazonHomePage;
import com.codetesters.automation.pages.SearchResultsPage;
import com.codetesters.automation.utils.ScreenshotUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SearchProductTest extends BaseTest {

    @Test(priority = 1, description = "Open Amazon and search for water purifier")
    public void searchWaterPurifier() {

        // Reset screenshot counter
        ScreenshotUtils.resetCounter();

        // Step 1: Open Amazon homepage
        AmazonHomePage homePage = new AmazonHomePage(driver);
        homePage.openAmazon();
        TestListener.logInfo("Opened Amazon.in homepage");

        // Screenshot 01 - Amazon Homepage
        ScreenshotUtils.captureScreenshot(driver, "amazon_homepage");
        TestListener.logInfo("Screenshot captured: amazon_homepage");

        // Verify homepage loaded
        String title = homePage.getPageTitle();
        Assert.assertTrue(title.toLowerCase().contains("amazon"),
                "Amazon homepage did not load correctly. Title: " + title);
        TestListener.logPass("Amazon homepage loaded successfully. Title: " + title);

        // Step 2: Search for "water purifier"
        homePage.searchProduct("water purifier");
        TestListener.logInfo("Searched for: water purifier");

        // Screenshot 02 - Search Results
        ScreenshotUtils.captureScreenshot(driver, "search_results");
        TestListener.logInfo("Screenshot captured: search_results");

        // Step 3: Verify search results are displayed
        SearchResultsPage resultsPage = new SearchResultsPage(driver);
        boolean resultsDisplayed = resultsPage.areResultsDisplayed();
        Assert.assertTrue(resultsDisplayed, "Search results were not displayed");
        TestListener.logPass("Search results displayed successfully");

        // Step 4: Verify at least 5 products are visible
        int productCount = resultsPage.getProductCount();
        Assert.assertTrue(productCount >= 5,
                "Expected at least 5 products, but found: " + productCount);
        TestListener.logPass("Found " + productCount + " products on the page");

        System.out.println("[" + getTimestamp() + "] ✅ Search completed — " + productCount + " products found");
    }
}
