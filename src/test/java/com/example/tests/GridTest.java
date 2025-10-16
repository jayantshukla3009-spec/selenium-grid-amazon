package com.example.tests;

import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GridTest {

    private URL hubUrl() throws Exception {
        return new URL("http://localhost:4444");
    }

    @DataProvider(name = "browsers", parallel = true)
    public Object[][] browsers() {
        return new Object[][]{
                {"chrome"},
                {"firefox"},
                {"edge"}
        };
    }

    @Test(dataProvider = "browsers", threadPoolSize = 3)
    public void openExampleDotCom(String browser) throws Exception {
        MutableCapabilities options;
        switch (browser.toLowerCase()) {
            case "firefox":
                options = new FirefoxOptions();
                break;
            case "edge":
                options = new EdgeOptions();
                break;
            default:
                options = new ChromeOptions();
        }

        RemoteWebDriver driver = new RemoteWebDriver(hubUrl(), options);
        try {
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            driver.get("https://example.com");
            String title = driver.getTitle();
            System.out.println(browser + " title -> " + title);
            Assert.assertTrue(title.toLowerCase().contains("example"),
                    "Title check failed for " + browser);
        } finally {
            driver.quit();
        }
    }
}
