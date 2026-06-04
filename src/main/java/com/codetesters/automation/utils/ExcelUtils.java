package com.codetesters.automation.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ExcelUtils {

    private static final String FILE_PATH = System.getProperty("user.dir") + "/testdata/PriceData.xlsx";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("dd-MMM HH:mm");

    public static void createExcelWithHeaders() {
        try {
            File dir = new File(System.getProperty("user.dir") + "/testdata");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("ProductData");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 11);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"S.No", "Product Name", "Price (₹)", "New Price (₹)", "Status", "Timestamp"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Column widths
            sheet.setColumnWidth(0, 2000);
            sheet.setColumnWidth(1, 15000);
            sheet.setColumnWidth(2, 4000);
            sheet.setColumnWidth(3, 4000);
            sheet.setColumnWidth(4, 4000);
            sheet.setColumnWidth(5, 5000);

            FileOutputStream fos = new FileOutputStream(FILE_PATH);
            workbook.write(fos);
            fos.close();
            workbook.close();

            System.out.println("[" + getTimestamp() + "] 💾 Excel file created: " + FILE_PATH);

        } catch (IOException e) {
            System.err.println("Error creating Excel file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void writeProductData(int rowNum, String productName, String price) {
        try {
            FileInputStream fis = new FileInputStream(FILE_PATH);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet("ProductData");

            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            Row row = sheet.createRow(rowNum);

            Cell cellSNo = row.createCell(0);
            cellSNo.setCellValue(rowNum);
            cellSNo.setCellStyle(dataStyle);

            Cell cellName = row.createCell(1);
            cellName.setCellValue(productName);
            cellName.setCellStyle(dataStyle);

            Cell cellPrice = row.createCell(2);
            cellPrice.setCellValue(price);
            cellPrice.setCellStyle(dataStyle);

            Cell cellNewPrice = row.createCell(3);
            cellNewPrice.setCellValue("—");
            cellNewPrice.setCellStyle(dataStyle);

            Cell cellStatus = row.createCell(4);
            cellStatus.setCellValue("Initial");
            cellStatus.setCellStyle(dataStyle);

            Cell cellTimestamp = row.createCell(5);
            cellTimestamp.setCellValue(LocalDateTime.now().format(TIMESTAMP_FORMAT));
            cellTimestamp.setCellStyle(dataStyle);

            fis.close();

            FileOutputStream fos = new FileOutputStream(FILE_PATH);
            workbook.write(fos);
            fos.close();
            workbook.close();

            System.out.println("[" + getTimestamp() + "] 💾 Saved to Excel — Row " + rowNum);

        } catch (IOException e) {
            System.err.println("Error writing to Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Map<String, String> readExistingPrices() {
        Map<String, String> prices = new HashMap<>();

        try {
            FileInputStream fis = new FileInputStream(FILE_PATH);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet("ProductData");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell nameCell = row.getCell(1);
                    Cell priceCell = row.getCell(2);

                    if (nameCell != null && priceCell != null) {
                        String name = nameCell.getStringCellValue();
                        String price = priceCell.getStringCellValue();
                        prices.put(name, price);
                    }
                }
            }

            fis.close();
            workbook.close();

            System.out.println("[" + getTimestamp() + "] 📖 Read " + prices.size() + " existing prices from Excel");

        } catch (IOException e) {
            System.err.println("Error reading Excel: " + e.getMessage());
            e.printStackTrace();
        }

        return prices;
    }

    /**
     * Updates the New Price column if the price has changed
     */
    public static void updateNewPrice(String productName, String newPrice) {
        try {
            FileInputStream fis = new FileInputStream(FILE_PATH);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet("ProductData");

            // Style for changed price
            CellStyle changedStyle = workbook.createCellStyle();
            Font changedFont = workbook.createFont();
            changedFont.setBold(true);
            changedFont.setColor(IndexedColors.ORANGE.getIndex());
            changedStyle.setFont(changedFont);
            changedStyle.setBorderBottom(BorderStyle.THIN);
            changedStyle.setBorderTop(BorderStyle.THIN);
            changedStyle.setBorderLeft(BorderStyle.THIN);
            changedStyle.setBorderRight(BorderStyle.THIN);

            CellStyle normalStyle = workbook.createCellStyle();
            normalStyle.setBorderBottom(BorderStyle.THIN);
            normalStyle.setBorderTop(BorderStyle.THIN);
            normalStyle.setBorderLeft(BorderStyle.THIN);
            normalStyle.setBorderRight(BorderStyle.THIN);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell nameCell = row.getCell(1);
                    if (nameCell != null && nameCell.getStringCellValue().equals(productName)) {
                        String oldPrice = row.getCell(2).getStringCellValue();

                        if (!oldPrice.equals(newPrice)) {
                            // Price changed
                            Cell newPriceCell = row.getCell(3);
                            if (newPriceCell == null) newPriceCell = row.createCell(3);
                            newPriceCell.setCellValue(newPrice);
                            newPriceCell.setCellStyle(changedStyle);

                            Cell statusCell = row.getCell(4);
                            if (statusCell == null) statusCell = row.createCell(4);
                            statusCell.setCellValue("Price Changed");
                            statusCell.setCellStyle(changedStyle);

                            Cell tsCell = row.getCell(5);
                            if (tsCell == null) tsCell = row.createCell(5);
                            tsCell.setCellValue(LocalDateTime.now().format(TIMESTAMP_FORMAT));
                            tsCell.setCellStyle(normalStyle);

                            System.out.println("[" + getTimestamp() + "] ⚠️ PRICE CHANGE: " + productName + " — " + oldPrice + " → " + newPrice);
                        } else {
                            // No change
                            Cell statusCell = row.getCell(4);
                            if (statusCell == null) statusCell = row.createCell(4);
                            statusCell.setCellValue("No Change");
                            statusCell.setCellStyle(normalStyle);

                            Cell tsCell = row.getCell(5);
                            if (tsCell == null) tsCell = row.createCell(5);
                            tsCell.setCellValue(LocalDateTime.now().format(TIMESTAMP_FORMAT));
                            tsCell.setCellStyle(normalStyle);

                            System.out.println("[" + getTimestamp() + "] ✅ No change: " + productName + " still " + oldPrice);
                        }
                        break;
                    }
                }
            }

            fis.close();
            FileOutputStream fos = new FileOutputStream(FILE_PATH);
            workbook.write(fos);
            fos.close();
            workbook.close();

        } catch (IOException e) {
            System.err.println("Error updating Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getFilePath() {
        return FILE_PATH;
    }

    private static String getTimestamp() {
        return java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
