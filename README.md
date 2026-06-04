# 🤖 CODETESTERS — HCL Automation Hackathon Framework

**Team:** CODETESTERS
**Event:** HCL Automation Hackathon
**Framework:** Selenium WebDriver + TestNG + POM + Data-Driven + ExtentReports

---

## 📁 Project Structure

```
HCL_Automation_Hackathon/
│
├── pom.xml                          ← Maven dependencies
├── testng.xml                       ← Suite runner + listener config
│
├── src/main/java/com/codetesters/automation/
│   ├── base/         BaseTest.java          ← WebDriver setup
│   ├── pages/        LoginPage.java         ← POM: Login screen
│   │                 DashboardPage.java     ← POM: Main workflow
│   ├── utils/        ExcelUtils.java        ← Excel reader (POI)
│   │                 ReportUtils.java       ← HTML report generator
│   │                 CreateTestData.java    ← Excel file generator
│   └── listeners/    TestListener.java      ← Auto reporting
│
├── src/test/java/com/codetesters/automation/tests/
│   ├── LoginTest.java                ← TC01, TC02, TC03
│   └── WorkflowTest.java             ← TC04, TC05, TC06, TC07
│
└── src/test/resources/
    ├── config.properties             ← URL, credentials, timeouts
    └── testdata/TestData.xlsx        ← Excel test data
```

---

## ⚡ Quick Setup (One-Time)

### Step 1 — Load Maven dependencies
```
Right-click pom.xml → Maven → Reload Project
```

### Step 2 — Generate TestData.xlsx
```
Right-click CreateTestData.java → Run 'CreateTestData.main()'
```

### Step 3 — Update config.properties
```properties
app.url=https://YOUR-APP-URL.com
valid.username=yourUsername
valid.password=yourPassword
```

### Step 4 — Run the full suite
```
Right-click testng.xml → Run
```

---

## 🔧 Technology Stack

| Tool | Version | Purpose |
|------|---------|---------|
| Java | 21 | Core language |
| Selenium WebDriver | 4.18.1 | Browser automation |
| TestNG | 7.9.0 | Test execution |
| Maven | 3.x | Build & dependencies |
| Apache POI | 5.2.5 | Excel data reading |
| ExtentReports | 5.1.1 | HTML reports |
| WebDriverManager | 5.8.0 | Auto driver setup |

---

## 📊 Test Cases

| ID | Test | Type |
|----|------|------|
| TC_01 | Valid Login | Positive |
| TC_02 | Invalid Login | Negative |
| TC_03 | Data-Driven Login | Data-Driven |
| TC_04 | End-to-End Workflow | E2E |
| TC_05 | Data-Driven Workflow | Data-Driven |
| TC_06 | Alert Handling | Functional |
| TC_07 | Dropdown Selection | Functional |

---

## 📄 Report
After test execution, the HTML report is at:
```
test-output/CODETESTERS_Report_<timestamp>.html
```

---

*Team CODETESTERS | HCL Automation Hackathon 🚀*
