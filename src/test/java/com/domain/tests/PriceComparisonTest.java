package com.domain.tests;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import com.domain.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.*;
import org.testng.Assert;

public class PriceComparisonTest {

    WebDriver driver;
    
    // List of URLs for the specific iPhone 16 Pro Max pages
    String[] urls = {
        "https://www.technodom.kz/astana/p/smartfon-apple-iphone-16-pro-max-256gb-desert-titanium-284651?recommended_by=full_search&recommended_code=iphone%2016%20pro%20max&source=search_results&suggest_item_index=1&rees_query_id=p7EeclJq5CZvORgkW2Ij1744620199",
        "https://www.sulpak.kz/g/smartfoniy-apple-iphone-16-pro-max-256gb-natural-titanium-mywy3hxa",
        "https://fmobile.kz/category/smartfony/smartfon-apple-iphone-16-pro-max-4881",
        "https://www.dns-shop.kz/product/1af577a810c10023/69-smartfon-apple-iphone-16-pro-max-256-gb-cernyj/",
        "https://ispace.kz/iphone/iphone-16-pro-max/iphone-16-pro-max-256-gb-black-titanium-mywv3hxa",
        "https://kaspi.kz/shop/p/apple-iphone-16-pro-max-256gb-zolotistyi-123890547/?c=710000000"
    };

    List<Double> prices = new ArrayList<>();
    List<String> siteNames = new ArrayList<>();

    @BeforeMethod
    public void setUp() {
        // Here you can choose the browser; using "chrome" for simplicity.
        driver = WebDriverFactory.getDriver("chrome");
    }

    @Test
    public void testPriceComparison() {
        for (String url : urls) {
            driver.get(url);
            
            // Optionally, wait for the page to load.
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            double price = extractPrice(driver, url);
            String siteName = getSiteName(url);
            System.out.println("Price from " + siteName + ": " + price);
            prices.add(price);
            siteNames.add(siteName);
        }
        
        // Ensure that some prices were extracted
        Assert.assertFalse(prices.isEmpty(), "No prices were extracted.");
        
        double sum = 0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for(double p : prices) {
            sum += p;
            if(p < min) {
                min = p;
            }
            if(p > max) {
                max = p;
            }
        }
        double avg = sum / prices.size();
        
        System.out.println("Cheapest Price: " + min);
        System.out.println("Average Price: " + avg);
        System.out.println("Most Expensive Price: " + max);
    }
    
    /**
     * Uses site-specific rules to extract the price from the page.
     * Adjust the CSS selectors as needed based on the actual page structure.
     */
    private double extractPrice(WebDriver driver, String url) {
        double price = 0;
        try {
            if(url.contains("technodom.kz")){
                // Example: adjust the CSS selector to match Technodom’s page structure
                WebElement priceElement = driver.findElement(By.cssSelector(".price"));
                price = parsePrice(priceElement.getText());
            } else if(url.contains("sulpak.kz")){
                // Example: adjust the CSS selector to match Sulpak’s page structure
                WebElement priceElement = driver.findElement(By.cssSelector(".price"));
                price = parsePrice(priceElement.getText());
            } else if(url.contains("fmobile.kz")){
                // Example: adjust the selector as needed
                WebElement priceElement = driver.findElement(By.cssSelector(".price"));
                price = parsePrice(priceElement.getText());
            } else if(url.contains("dns-shop.kz")){
                // Example: adjust the CSS selector based on the DNS-Shop page markup
                WebElement priceElement = driver.findElement(By.cssSelector(".product-price"));
                price = parsePrice(priceElement.getText());
            } else if(url.contains("ispace.kz")){
                // Example: adjust as needed for iSpace
                WebElement priceElement = driver.findElement(By.cssSelector(".price"));
                price = parsePrice(priceElement.getText());
            } else if(url.contains("kaspi.kz")){
                // Example: adjust as needed for Kaspi
                WebElement priceElement = driver.findElement(By.cssSelector(".price"));
                price = parsePrice(priceElement.getText());
            } else {
                System.out.println("No extraction rule defined for URL: " + url);
            }
        } catch(Exception e) {
            System.out.println("Failed to extract price from " + url + ": " + e.getMessage());
        }
        return price;
    }
    
    /**
     * Parses the price string by removing non-numeric characters.
     * You may need to tweak this method if the price uses commas or other separators.
     */
    private double parsePrice(String priceText) {
        // Remove all non-numeric characters (keeping dots and commas)
        priceText = priceText.replaceAll("[^0-9.,]", "");
        // If the price uses a comma as a decimal separator, convert it to a dot
        if(priceText.contains(",")) {
            priceText = priceText.replace(",", ".");
        }
        try {
            return Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            System.out.println("Error parsing price: " + priceText);
            return 0;
        }
    }
    
    /**
     * Returns a friendly name for the site based on the URL.
     */
    private String getSiteName(String url) {
        if(url.contains("technodom.kz"))
            return "Technodom";
        else if(url.contains("sulpak.kz"))
            return "Sulpak";
        else if(url.contains("fmobile.kz"))
            return "FMobile";
        else if(url.contains("dns-shop.kz"))
            return "DNS-Shop";
        else if(url.contains("ispace.kz"))
            return "iSpace";
        else if(url.contains("kaspi.kz"))
            return "Kaspi";
        else
            return "Unknown Site";
    }
    
    @AfterMethod
    public void tearDown() {
        if(driver != null) {
            driver.quit();
        }
    }
}