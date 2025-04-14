package org.example;

import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginTest {

    private WebDriver driver;
    private static final Logger logger = Logger.getLogger(LoginTest.class.getName());
    private String browser;

    /**
     * Constructor that accepts browser parameter for factory pattern
     *
     * @param browser The browser to use for testing ("chrome" or "firefox")
     */
    public LoginTest(String browser) {
        this.browser = browser;
    }

    @BeforeMethod
    public void setUp() {
        logger.info("Setting up test with browser: " + browser);
        if (browser.equalsIgnoreCase("chrome")) {
            logger.info("Initializing Chrome WebDriver");
            WebDriverManager.chromedriver().browserVersion("auto").setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
            driver = new ChromeDriver(options);
            logger.info("Chrome WebDriver initialized successfully");
        } else if (browser.equalsIgnoreCase("firefox")) {
            logger.info("Initializing Firefox WebDriver");
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--headless");
            driver = new FirefoxDriver(options);
            logger.info("Firefox WebDriver initialized successfully");
        } else {
            logger.severe("Unsupported browser: " + browser);
            throw new IllegalArgumentException("Browser " + browser + " is not supported");
        }

        // Navigate to the login page
        logger.info("Navigating to login page");
        driver.get("https://practicetestautomation.com/practice-test-login/");
        logger.info("Navigation complete");
    }

    @Test
    public void testValidLogin() {
        logger.info("Starting valid login test");

        // Enter valid credentials
        logger.info("Locating username field");
        WebElement usernameField = driver.findElement(By.id("username"));
        logger.info("Locating password field");
        WebElement passwordField = driver.findElement(By.id("password"));
        logger.info("Locating login button");
        WebElement loginButton = driver.findElement(By.id("submit"));

        logger.info("Entering username: student");
        usernameField.sendKeys("student");
        logger.info("Entering password: Password123");
        passwordField.sendKeys("Password123");
        logger.info("Clicking login button");
        loginButton.click();
        logger.info("Login form submitted");

        // Verify successful login
        logger.info("Verifying successful login");
        logger.info("Looking for success message");
        WebElement successMessage = driver.findElement(By.cssSelector(".post-title"));
        Assert.assertTrue(successMessage.isDisplayed(), "Success message should be displayed");
        logger.info("Success message is displayed: " + successMessage.getText());
        Assert.assertEquals(successMessage.getText(), "Logged In Successfully",
                "Success message should indicate successful login");

        // Additional verification - check if logout button is present
        logger.info("Looking for logout button");
        WebElement logoutButton = driver.findElement(By.linkText("Log out"));
        Assert.assertTrue(logoutButton.isDisplayed(), "Logout button should be displayed after successful login");
        logger.info("Logout button is displayed");
        logger.info("Valid login test completed successfully");
    }

    @Test
    public void testInvalidUsername() {
        logger.info("Starting invalid username test");

        // Enter invalid username
        logger.info("Locating username field");
        WebElement usernameField = driver.findElement(By.id("username"));
        logger.info("Locating password field");
        WebElement passwordField = driver.findElement(By.id("password"));
        logger.info("Locating login button");
        WebElement loginButton = driver.findElement(By.id("submit"));

        logger.info("Entering invalid username: wronguser");
        usernameField.sendKeys("wronguser");
        logger.info("Entering password: Password123");
        passwordField.sendKeys("Password123");
        logger.info("Clicking login button");
        loginButton.click();
        logger.info("Login form submitted");

        // Verify error message
        logger.info("Verifying error message for invalid username");
        WebElement errorMessage = driver.findElement(By.id("error"));
        Assert.assertTrue(errorMessage.isDisplayed(), "Error message should be displayed");
        logger.info("Error message is displayed: " + errorMessage.getText());
        Assert.assertEquals(errorMessage.getText(), "Your username is invalid!",
                "Error message should indicate invalid username");
        logger.info("Invalid username test completed successfully");
    }

    @Test
    public void testInvalidPassword() {
        logger.info("Starting invalid password test");

        // Enter invalid password
        logger.info("Locating username field");
        WebElement usernameField = driver.findElement(By.id("username"));
        logger.info("Locating password field");
        WebElement passwordField = driver.findElement(By.id("password"));
        logger.info("Locating login button");
        WebElement loginButton = driver.findElement(By.id("submit"));

        logger.info("Entering username: student");
        usernameField.sendKeys("student");
        logger.info("Entering invalid password: wrongpassword");
        passwordField.sendKeys("wrongpassword");
        logger.info("Clicking login button");
        loginButton.click();
        logger.info("Login form submitted");

        // Verify error message
        logger.info("Verifying error message for invalid password");
        WebElement errorMessage = driver.findElement(By.id("error"));
        Assert.assertTrue(errorMessage.isDisplayed(), "Error message should be displayed");
        logger.info("Error message is displayed: " + errorMessage.getText());
        Assert.assertEquals(errorMessage.getText(), "Your password is invalid!",
                "Error message should indicate invalid password");
        logger.info("Invalid password test completed successfully");
    }

    @AfterMethod
    public void tearDown() {
        logger.info("Tearing down test");
        if (driver != null) {
            logger.info("Quitting WebDriver");
            driver.quit();
            logger.info("WebDriver quit successfully");
        }
        logger.info("Test cleanup complete");
    }
}
