# Selenium WebDriver and TestNG Homework

This project demonstrates the use of Selenium WebDriver and TestNG for automated testing. It includes three main components:

1. **Price Comparison Test**: Connects to three different websites to compare the price of an iPhone.
2. **Login Test**: Tests a login interface across two different browsers (Chrome and Firefox).
3. **Factory Test**: Demonstrates the use of TestNG's @Factory annotation.

## Project Structure

```
├── src
│   ├── main
│   │   └── resources
│   │       └── login.html       # Simple login page for testing
│   └── test
│       └── java
│           └── org
│               └── example
│                   ├── PriceComparisonTest.java  # Task 1: Price comparison
│                   ├── LoginTest.java            # Task 2: Login testing
│                   └── FactoryTest.java          # Task 3: Factory annotation
├── testng.xml      # TestNG configuration file
├── pom.xml         # Maven project configuration
└── README.md       # This file
```

## Prerequisites

- Java JDK 11 or higher
- Maven
- Chrome and Firefox browsers installed

## Running the Tests

To run all tests, use the following command:

```bash
mvn clean test
```

This will execute all the tests defined in the `testng.xml` file.

## Test Descriptions

### 1. Price Comparison Test

The `PriceComparisonTest` class connects to three different e-commerce websites (Amazon, Best Buy, and Walmart) to compare the price of an iPhone. It generates a report showing:
- The cheapest price and which website offers it
- The most expensive price and which website offers it
- The average price across all websites

### 2. Login Test

The `LoginTest` class tests a login interface with both valid and invalid credentials. It runs on both Chrome and Firefox browsers to ensure cross-browser compatibility. The test cases include:
- Successful login with valid credentials
- Failed login with invalid username
- Failed login with invalid password

### 3. Factory Test

The `FactoryTest` class demonstrates the use of TestNG's @Factory annotation to create multiple test instances with different parameters. This shows how to run the same test with different input data.

## Notes

- The tests run in headless mode by default to improve performance.
- The price comparison test may need adjustments based on website changes.
- The login test uses a demo website (The Internet Herokuapp) for testing.