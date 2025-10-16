package com.example.tests;

import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AmazonTest {

    @DataProvider(name = "browsers", parallel = true)
    public Object[][] getBrowsers() {
        return new Object[][]{
                {"chrome"},
                {"firefox"},
                {"edge"}
        };
    }

    @Test(dataProvider = "browsers")
    public void testAmazonSearch(String browser) throws Exception {
        URL gridUrl = new URL("http://host.docker.internal:4444/wd/hub\r\n"
        		+ ""); // Selenium Grid hub URL

        // Browser setup
        RemoteWebDriver driver = null;
        switch (browser.toLowerCase()) {
            case "chrome":
                driver = new RemoteWebDriver(gridUrl, new ChromeOptions());
                break;
            case "firefox":
                driver = new RemoteWebDriver(gridUrl, new FirefoxOptions());
                break;
            case "edge":
                driver = new RemoteWebDriver(gridUrl, new EdgeOptions());
                break;
        }

        // Maximize and set timeouts
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Navigate to Amazon
        driver.get("https://www.amazon.in/");
        Thread.sleep(5000); // wait for page to load

        // Search for a product
        WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
        searchBox.sendKeys("wireless mouse");
        searchBox.submit();

        Thread.sleep(5000); // allow search results to load

        // Validate page title contains "mouse"
        String title = driver.getTitle();
        System.out.println(browser + " → " + title);
        Assert.assertTrue(title.toLowerCase().contains("mouse"), "Title validation failed for " + browser);

        driver.quit();
    }
}
