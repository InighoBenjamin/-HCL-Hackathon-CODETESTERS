package com.codetesters.automation.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ExcelUtils — Reads test data from .xlsx files for Data-Driven Testing.
 *
 * Usage in @Test:
 *   @DataProvider(name = "loginData")
 *   public Object[][] getData() {
 *       return ExcelUtils.getTestData("LoginData");
 *   }
 *
 * Excel file location: src/test/resources/testdata/TestData.xlsx
 * Sheet name must match exactly (case-sensitive).
 *
 * Curriculum: Day 12 (Apache POI — Data-Driven Testing)
 * Team: CODETESTERS | HCL Automation Hackathon
 */
public class ExcelUtils {

    private static final String FILE_PATH = "src/test/resources/testdata/TestData.xlsx";

    /**
     * Reads all data rows from the given sheet.
     * Row 0 (header row) is automatically skipped.
     *
     * @param sheetName - Excel sheet tab name
     * @return Object[][] - 2D array usable by TestNG @DataProvider
     */
    public static Object[][] getTestData(String sheetName) {
        List<Object[]> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                throw new RuntimeException("[ExcelUtils] Sheet '" + sheetName + "' not found in TestData.xlsx");
            }

            int lastRow = sheet.getLastRowNum();
            int lastCol = sheet.getRow(0).getLastCellNum();

            // Start from row 1 — row 0 is the header
            for (int r = 1; r <= lastRow; r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                Object[] rowData = new Object[lastCol];
                for (int c = 0; c < lastCol; c++) {
                    rowData[c] = getCellValue(row.getCell(c));
                }
                data.add(rowData);
            }

        } catch (IOException e) {
            throw new RuntimeException("[ExcelUtils] Cannot read file: " + FILE_PATH + " → " + e.getMessage());
        }

        System.out.println("[ExcelUtils] ✅ Loaded " + data.size() + " rows from sheet: " + sheetName);
        return data.toArray(new Object[0][]);
    }

    // ── Handles all Excel cell types safely ──────────────────────────────────

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                double val = cell.getNumericCellValue();
                yield (val == Math.floor(val)) ? String.valueOf((long) val) : String.valueOf(val);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default      -> "";
        };
    }
}
