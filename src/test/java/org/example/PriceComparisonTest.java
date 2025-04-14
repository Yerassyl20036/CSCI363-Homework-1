package org.example;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class PriceComparisonTest {

    private WebDriver driver;
    private List<Double> prices = new ArrayList<>();
    private List<String> websites = new ArrayList<>();

    @BeforeMethod
    public void setUp() {
        WebDriverManager.firefoxdriver().browserVersion("auto").setup();
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless"); // Run in headless mode
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        driver = new FirefoxDriver(options);

        // Set page load timeout
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        // Set implicit wait for all elements
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    /**
     * Waits for page to be fully loaded with JavaScript
     */
    private void waitForPageToLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(30)).until(webDriver
                -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

        // Additional small delay to ensure dynamic content is loaded
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Gets price with retry mechanism and explicit wait
     */
    private double getPriceWithWait(String url, String cssSelector, String website) {
        int maxRetries = 3;
        int retryCount = 0;

        while (retryCount < maxRetries) {
            try {
                driver.get(url);

                // Wait for page to fully load
                waitForPageToLoad();

                // Wait for price element to be visible
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
                WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelector)));

                // Scroll to element to ensure it's in view
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", priceElement);

                String priceText = priceElement.getText().replaceAll("[^\\d.]", "");
                if (priceText.isEmpty()) {
                    throw new Exception("Price text is empty");
                }

                double price = Double.parseDouble(priceText);
                // Convert from Tenge to USD (approximate conversion for comparison)
                // Using a rough conversion rate of 450 Tenge = 1 USD
                price = price / 450;
                prices.add(price);
                websites.add(website);
                System.out.println(website + " iPhone price: $" + String.format("%.2f", price) + " (converted from ₸)");
                return price;
            } catch (Exception e) {
                retryCount++;
                System.out.println("Attempt " + retryCount + " failed for " + website + ": " + e.getMessage());
                if (retryCount >= maxRetries) {
                    System.out.println("Error getting price from " + website + " after " + maxRetries + " attempts: " + e.getMessage());
                }
            }
        }
        return 0;
    }

    @Test
    public void compareIPhonePrices() {
        // Website 1: DNS Shop Kazakhstan
        getPriceWithWait(
                "https://www.dns-shop.kz/product/da863c1ba0ff6200/61-smartfon-apple-iphone-13-128-gb-zelenyj/",
                ".product-buy__price",
                "DNS Shop"
        );

        // Website 2: Kaspi.kz
        getPriceWithWait(
                "https://kaspi.kz/shop/p/apple-iphone-13-128gb-chernyi-102298404/?srsltid=AfmBOopJFvt4P_gtaOnhGQYR8BMIHn5KM3ixF-U7vOZxBNTooZ_zJdLk",
                ".item__price-once",
                "Kaspi.kz"
        );

        // Website 3: Sulpak.kz
        getPriceWithWait(
                "https://www.sulpak.kz/g/smartfon_apple_iphone_13_128gb_midnight_mlnw3rka",
                ".product__price",
                "Sulpak.kz"
        );

        // Generate price comparison report
        generatePriceReport();
    }

    private void generatePriceReport() {
        if (prices.isEmpty()) {
            System.out.println("No prices were found to compare.");
            return;
        }

        // Find cheapest, most expensive, and calculate average
        double cheapest = Collections.min(prices);
        double mostExpensive = Collections.max(prices);
        double sum = 0;
        for (double price : prices) {
            sum += price;
        }
        double average = sum / prices.size();

        // Find which website has the cheapest price
        int cheapestIndex = prices.indexOf(cheapest);
        String cheapestWebsite = websites.get(cheapestIndex);

        // Find which website has the most expensive price
        int expensiveIndex = prices.indexOf(mostExpensive);
        String expensiveWebsite = websites.get(expensiveIndex);

        // Print report
        System.out.println("\n===== iPhone Price Comparison Report =====");
        System.out.println("Cheapest: $" + cheapest + " at " + cheapestWebsite);
        System.out.println("Most Expensive: $" + mostExpensive + " at " + expensiveWebsite);
        System.out.println("Average Price: $" + String.format("%.2f", average));
        System.out.println("======================================\n");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
