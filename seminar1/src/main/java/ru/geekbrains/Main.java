package ru.geekbrains;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class Main {
    public static void main(String[] args) {
       String pathToChromeDriver = "src\\main\\resources\\chromedriver.exe";
       String pathToGeckoDriver = "src\\main\\resources\\geckodriver.exe";
       System.setProperty("webdriver.chrome.driver", pathToChromeDriver);
       System.setProperty("webdriver.firefox.driver", pathToGeckoDriver);

        ChromeOptions options = new ChromeOptions();
//        FirefoxOptions options = new FirefoxOptions();

        options.addArguments("--headless");

        WebDriver driver = new ChromeDriver(options);
//        WebDriver driver = new FirefoxDriver(options);

        driver.manage().window().maximize();
        driver.get("https://www.google.com ");
        System.out.println("Page title" + driver.getTitle());

        driver.quit();
    }
}