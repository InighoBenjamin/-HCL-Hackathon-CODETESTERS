package com.codetesters.automation.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * CreateTestData — Auto-generates TestData.xlsx with sample data.
 *
 * ┌──────────────────────────────────────────────────────────┐
 * │  HACKATHON DAY: Run this class ONCE to create the Excel  │
 * │  file. Then edit TestData.xlsx with real use case data.  │
 * │  Right-click this file → Run 'CreateTestData.main()'     │
 * └──────────────────────────────────────────────────────────┘
 *
 * Creates: src/test/resources/testdata/TestData.xlsx
 *   Sheet 1 → LoginData    (username, password, expectedResult)
 *   Sheet 2 → WorkflowData (searchTerm, category, expectedMessage)
 *
 * Curriculum: Day 12 (Apache POI — Writing Excel files)
 * Team: CODETESTERS | HCL Automation Hackathon
 */
public class CreateTestData {

    private static final String FILE_PATH = "src/test/resources/testdata/TestData.xlsx";

    public static void main(String[] args) {
        createTestDataFile();
    }

    public static void createTestDataFile() {
        // Create directory if it doesn't exist
        new File("src/test/resources/testdata/").mkdirs();

        try (Workbook workbook = new XSSFWorkbook()) {

            // ── Sheet 1: LoginData ────────────────────────────────────────────
            Sheet loginSheet = workbook.createSheet("LoginData");

            // Header row style
            CellStyle headerStyle = createHeaderStyle(workbook);

            // Header row
            Row loginHeader = loginSheet.createRow(0);
            createStyledCell(loginHeader, 0, "Username",       headerStyle);
            createStyledCell(loginHeader, 1, "Password",       headerStyle);
            createStyledCell(loginHeader, 2, "ExpectedResult", headerStyle);

            // Data rows — 🔧 CHANGE THESE on hackathon day with real credentials
            String[][] loginData = {
                {"admin",     "admin123",  "success"},
                {"testuser",  "test123",   "success"},
                {"wronguser", "wrongpass", "failure"},
                {"",          "",          "failure"},   // empty credentials
            };

            for (int i = 0; i < loginData.length; i++) {
                Row row = loginSheet.createRow(i + 1);
                for (int j = 0; j < loginData[i].length; j++) {
                    row.createCell(j).setCellValue(loginData[i][j]);
                }
            }

            // Auto-size columns
            for (int i = 0; i < 3; i++) loginSheet.autoSizeColumn(i);

            // ── Sheet 2: WorkflowData ─────────────────────────────────────────
            Sheet workflowSheet = workbook.createSheet("WorkflowData");

            Row workflowHeader = workflowSheet.createRow(0);
            createStyledCell(workflowHeader, 0, "SearchTerm",      headerStyle);
            createStyledCell(workflowHeader, 1, "Category",        headerStyle);
            createStyledCell(workflowHeader, 2, "ExpectedMessage", headerStyle);

            // Data rows — 🔧 CHANGE THESE on hackathon day with real use case data
            String[][] workflowData = {
                {"Laptop",     "Electronics", "Order placed successfully!"},
                {"Phone",      "Mobile",      "Order placed successfully!"},
                {"Headphones", "Accessories", "Order placed successfully!"},
            };

            for (int i = 0; i < workflowData.length; i++) {
                Row row = workflowSheet.createRow(i + 1);
                for (int j = 0; j < workflowData[i].length; j++) {
                    row.createCell(j).setCellValue(workflowData[i][j]);
                }
            }

            for (int i = 0; i < 3; i++) workflowSheet.autoSizeColumn(i);

            // ── Write to file ─────────────────────────────────────────────────
            try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
                workbook.write(fos);
            }

            System.out.println("✅ TestData.xlsx created successfully at: " + FILE_PATH);
            System.out.println("   → Sheet 1: LoginData    (4 rows)");
            System.out.println("   → Sheet 2: WorkflowData (3 rows)");
            System.out.println("\n🔧 Edit the Excel file with your actual hackathon test data!");

        } catch (IOException e) {
            System.err.println("❌ Failed to create TestData.xlsx: " + e.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private static void createStyledCell(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }
}
