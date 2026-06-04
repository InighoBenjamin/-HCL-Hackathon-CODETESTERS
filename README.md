# Amazon Price Tracker — CODETESTERS

## HCL GUVI Hackathon 2026

Automated Product Search, Price Capture & Change Tracking using Selenium Java POM Framework.

---

## Project Overview

This project automates the end-to-end workflow of:
1. Opening Amazon India (amazon.in)
2. Searching for "water purifier"
3. Checking product availability
4. Capturing product name & price for the first 5 available products
5. Taking screenshots at every step
6. Saving data to Excel (PriceData.xlsx)
7. Tracking price changes and updating the Excel
8. Generating HTML test reports using ExtentReports

---

## Tech Stack

| Technology       | Version | Purpose                        |
|-----------------|---------|--------------------------------|
| Java            | 11      | Core programming language       |
| Selenium        | 4.15.0  | Browser automation              |
| TestNG          | 7.8.0   | Test framework & assertions     |
| Apache POI      | 5.2.5   | Excel read/write operations     |
| ExtentReports   | 5.1.1   | HTML test report generation     |
| Maven           | 3.x     | Build & dependency management   |

---

## How to Run

1. Open the project in IntelliJ IDEA
2. Right-click `testng.xml` → Run
3. Or run via command line:
   ```
   mvn clean test
   ```

---

## Test Cases

| TC   | Class                      | Description                              |
|------|----------------------------|------------------------------------------|
| TC01 | SearchProductTest          | Open Amazon, search "water purifier"     |
| TC02 | CaptureProductDataTest     | Capture 5 products, save to Excel        |
| TC03 | PriceChangeTrackerTest     | Re-check prices, update Excel if changed |

---

## Outputs

- **PriceData.xlsx** — Product names, prices, change tracking
- **Screenshots** — 9+ step-wise PNG captures
- **ExtentReport.html** — Professional HTML test report

---

## Team CODETESTERS

| Member   | Role           |
|----------|---------------|
| Member 1 | Team Lead      |
| Member 2 | POM Developer  |
| Member 3 | Data Engineer  |
| Member 4 | Test Engineer  |
| Member 5 | Report Lead    |

---

© 2026 CODETESTERS — HCL GUVI Hackathon
