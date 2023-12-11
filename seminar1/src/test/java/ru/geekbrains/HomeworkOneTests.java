package ru.geekbrains;


import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomeworkOneTests {

    private WebDriver driver;
    private static ChromeOptions options;
    private static WebDriverWait wait;
    private static final String USERNAME = "Student-7";
    private static final String PASSWORD = "2e16f2452e";


    @BeforeAll
    public static void setupClass() {
        options = new ChromeOptions();
        String pathToChromeDriver = "src/main/resources/chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", pathToChromeDriver);
    }

    @BeforeEach
    public void setupTest() {
        // Создаем экземпляр драйвера
        driver = new ChromeDriver(options);

        // Создаем экземпляр вейтера
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));


        // Растягиваем окно браузера на весь экран
        driver.manage().window().maximize();
    }

    @Test
    public void testCreateGroup() {
        login();
        // Клик на кнопку создания группы
        WebElement createGroupBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#create-btn")));
        createGroupBtn.click();

        // Ввод текста в поле названия группы
        String newNameGroup = "GROUP" + System.currentTimeMillis();
        WebElement createGroupField = wait.until(ExpectedConditions.elementToBeClickable
                (By.cssSelector("#update-item input")));
        createGroupField.sendKeys(newNameGroup);

        // Клик на кнопку создания
        WebElement createBtn = wait.until(ExpectedConditions.elementToBeClickable
                (By.cssSelector("#update-item button")));
        createBtn.click();

        // Закрыть окно
        WebElement closeCreateWindow = wait.until(ExpectedConditions.elementToBeClickable
                (By.cssSelector("button[data-mdc-dialog-action='close']")));
        closeCreateWindow.click();
        driver.navigate().refresh();
        // Проверить что группа создана
        WebElement nameGroup = wait.until(ExpectedConditions.presenceOfElementLocated
                (By.cssSelector("table tbody tr td:nth-of-type(2)")));
        String actualNameGroup = nameGroup.getText();
        Assertions.assertEquals(newNameGroup, actualNameGroup);

        makeScreenshot();

    }

    public void makeScreenshot() {
        // Делаем скриншот
        File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
            // Копируем скриншот в нужное место
            FileUtils.copyFile(screenshot,
                    new File("C:/Users/user/Desktop/GB/JavaWebAUto/seminar1/src/main/resources/screen.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login() {

        // Навигация на  https://test-stand.gb.ru/login
        driver.get("https://test-stand.gb.ru/login");

        // Поиск полей для ввода username и password
        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated
                (By.cssSelector("form#login input[type='text']")));
        WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated
                (By.cssSelector("form#login input[type='password']")));

        // Ввод username и password
        usernameField.sendKeys(USERNAME);
        passwordField.sendKeys(PASSWORD);

        // Нажатие кнопки login
        WebElement loginButton = wait.until(ExpectedConditions.presenceOfElementLocated
                (By.cssSelector("form#login button")));
        loginButton.click();
        wait.until(ExpectedConditions.invisibilityOf(loginButton));

        // Проверка что логин прошел успешно
        WebElement usernameLink = wait.until(ExpectedConditions.presenceOfElementLocated
                (By.partialLinkText(USERNAME)));
        String actualUsername = usernameLink.getText().replace("\n", "").trim();
        Assertions.assertEquals(String.format("Hello, %s", USERNAME), actualUsername);


    }

    @AfterEach
    public void teardown() {
        // Закрываем все окна браузера и процесс драйвера
        driver.quit();
    }
}
