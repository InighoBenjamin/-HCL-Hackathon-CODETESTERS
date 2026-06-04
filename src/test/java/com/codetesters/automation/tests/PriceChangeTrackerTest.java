package com.codetesters.automation.tests;

import com.codetesters.automation.base.BaseTest;
import com.codetesters.automation.listeners.TestListener;
import com.codetesters.automation.pages.AmazonHomePage;
import com.codetesters.automation.pages.SearchResultsPage;
import com.codetesters.automation.utils.ExcelUtils;
import com.codetesters.automation.utils.ScreenshotUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class PriceChangeTrackerTest extends BaseTest {

    @Test(priority = 3, description = "Re-check prices and update Excel if any price has changed")
    public void trackPriceChanges() {

        // Step 1: Read existing prices from Excel
        Map<String, String> savedPrices = ExcelUtils.readExistingPrices();
        Assert.assertFalse(savedPrices.isEmpty(),
                "No existing price data found in Excel. Run CaptureProductDataTest first.");
        TestListener.logInfo("Loaded " + savedPrices.size() + " saved prices from Excel");

        System.out.println();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("  🔄 PRICE RE-CHECK");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println();

        // Step 2: Re-navigate to Amazon and search again
        AmazonHomePage homePage = new AmazonHomePage(driver);
        homePage.openAmazon();
        homePage.searchProduct("water purifier");
        TestListener.logInfo("Re-navigated to Amazon, searched water purifier again");

        // Screenshot - Price recheck
        ScreenshotUtils.captureScreenshot(driver, "price_recheck");

        // Step 3: Scrape current prices
        SearchResultsPage resultsPage = new SearchResultsPage(driver);
        List<Map<String, String>> currentProducts = resultsPage.captureFirst5Products();

        // Step 4: Compare current prices with saved prices
        int changesDetected = 0;
        int productsChecked = 0;

        for (Map<String, String> product : currentProducts) {
            String currentName = product.get("name");
            String currentPrice = product.get("price");

            if (savedPrices.containsKey(currentName)) {
                String savedPrice = savedPrices.get(currentName);
                productsChecked++;

                // Update Excel
                ExcelUtils.updateNewPrice(currentName, currentPrice);

                if (!savedPrice.equals(currentPrice)) {
                    changesDetected++;
                    TestListener.logWarning("⚠️ PRICE CHANGE: " + currentName + " — " + savedPrice + " → " + currentPrice);
                } else {
                    TestListener.logPass("✅ No change: " + currentName + " still " + savedPrice);
                }
            }
        }

        // Step 5: Take screenshot if any price changed
        if (changesDetected > 0) {
            ScreenshotUtils.captureScreenshot(driver, "price_change_detected");
            System.out.println("[" + getTimestamp() + "] ⚠️ " + changesDetected + " price change(s) detected!");
        } else {
            System.out.println("[" + getTimestamp() + "] ✅ No price changes detected — all prices stable");
        }

        // Step 6: Final assertions
        Assert.assertTrue(productsChecked > 0,
                "No products were matched for price comparison");
        TestListener.logPass("Price tracking complete. Checked: " + productsChecked + ", Changes: " + changesDetected);

        System.out.println("[" + getTimestamp() + "] 💾 Excel updated with latest price data");
        System.out.println("[" + getTimestamp() + "] ✅ Price tracking complete!");
    }
}
