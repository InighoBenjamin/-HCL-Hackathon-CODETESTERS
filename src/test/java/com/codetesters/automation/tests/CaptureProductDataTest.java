package com.codetesters.automation.tests;

import com.codetesters.automation.base.BaseTest;
import com.codetesters.automation.listeners.TestListener;
import com.codetesters.automation.pages.AmazonHomePage;
import com.codetesters.automation.pages.SearchResultsPage;
import com.codetesters.automation.utils.ExcelUtils;
import com.codetesters.automation.utils.ScreenshotUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

public class CaptureProductDataTest extends BaseTest {

    @Test(priority = 2, description = "Capture first 5 available product names and prices, save to Excel")
    public void captureProductData() {

        // Step 1: Navigate and search
        AmazonHomePage homePage = new AmazonHomePage(driver);
        homePage.openAmazon();
        homePage.searchProduct("water purifier");
        TestListener.logInfo("Navigated to Amazon and searched for water purifier");

        // Screenshot - Search results loaded
        ScreenshotUtils.captureScreenshot(driver, "search_results_for_capture");

        // Step 2: Create Excel file with headers
        ExcelUtils.createExcelWithHeaders();
        TestListener.logInfo("Excel file created with headers");

        // Step 3: Capture first 5 available products
        SearchResultsPage resultsPage = new SearchResultsPage(driver);
        List<Map<String, String>> products = resultsPage.captureFirst5Products();

        // Step 4: Write each product to Excel and take screenshots
        int rowNum = 1;
        for (Map<String, String> product : products) {
            String name = product.get("name");
            String price = product.get("price");

            // Write to Excel
            ExcelUtils.writeProductData(rowNum, name, price);
            TestListener.logInfo("Product " + rowNum + ": " + name + " — " + price);

            // Take screenshot for each product captured
            ScreenshotUtils.captureScreenshot(driver, "product_" + rowNum + "_captured");

            rowNum++;
        }

        // Step 5: Assertions
        Assert.assertEquals(products.size(), 5,
                "Expected 5 products but captured: " + products.size());
        TestListener.logPass("All 5 products captured successfully");

        // Verify Excel file exists
        File excelFile = new File(ExcelUtils.getFilePath());
        Assert.assertTrue(excelFile.exists(), "Excel file was not created");
        TestListener.logPass("Excel file created at: " + ExcelUtils.getFilePath());

        System.out.println("[" + getTimestamp() + "] ✅ All 5 products captured and saved to Excel!");
    }
}
