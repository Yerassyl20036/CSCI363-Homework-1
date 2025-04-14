package org.example;

import org.testng.annotations.Factory;

/**
 * Factory class for LoginTest that creates multiple instances with different
 * browser parameters. This demonstrates the TestNG Factory pattern for
 * parameterized test execution.
 */
public class LoginTestFactory {

    @Factory
    public Object[] createInstances() {
        return new Object[]{
            // Create instances for Chrome browser tests
            new LoginTest("chrome"),
            // Create instances for Firefox browser tests
            new LoginTest("firefox")
        };
    }
}
