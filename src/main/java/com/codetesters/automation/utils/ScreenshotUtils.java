package com.codetesters.automation.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {

    private static final String SCREENSHOT_DIR = System.getProperty("user.dir") + "/screenshots/";
    private static int stepCounter = 0;

    /**
     * Captures a screenshot and saves it with a step number and name
     */
    public static String captureScreenshot(WebDriver driver, String stepName) {
        stepCounter++;
        String formattedStep = String.format("%02d", stepCounter);
        String fileName = formattedStep + "_" + stepName + ".png";
        String filePath = SCREENSHOT_DIR + fileName;

        try {
            File dir = new File(SCREENSHOT_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            FileUtils.copyFile(srcFile, destFile);

            System.out.println("[" + getTimestamp() + "] 📸 Screenshot saved: " + fileName);

        } catch (IOException e) {
            System.err.println("Error capturing screenshot: " + e.getMessage());
            e.printStackTrace();
        }

        return filePath;
    }

    /**
     * Captures a screenshot with a timestamp in the filename
     */
    public static String captureTimestampedScreenshot(WebDriver driver, String stepName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
        String fileName = stepName + "_" + timestamp + ".png";
        String filePath = SCREENSHOT_DIR + fileName;

        try {
            File dir = new File(SCREENSHOT_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            FileUtils.copyFile(srcFile, destFile);

            System.out.println("[" + getTimestamp() + "] 📸 Screenshot saved: " + fileName);

        } catch (IOException e) {
            System.err.println("Error capturing screenshot: " + e.getMessage());
            e.printStackTrace();
        }

        return filePath;
    }

    public static void resetCounter() {
        stepCounter = 0;
    }

    public static String getScreenshotDir() {
        return SCREENSHOT_DIR;
    }

    public static int getStepCount() {
        return stepCounter;
    }

    private static String getTimestamp() {
        return java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
