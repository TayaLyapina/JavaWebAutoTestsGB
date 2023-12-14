package gb;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Пример использования самых базовых методов библиотеки Selenium.
 */
public class TestStandGB {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String USERNAME = "Student-7";
    private static final String PASSWORD =  "2e16f2452e";
    private static final String urlBase = "https://test-stand.gb.ru/login";
    private static final int number = 6;

    @BeforeAll
    public static void setupClass() {
        String pathToChromeDriver = "src/main/resources/chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", pathToChromeDriver);
    }

    @BeforeEach
    public void setupTest() {
        // Создаём экземпляр драйвера
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        // Растягиваем окно браузера на весь экран
        driver.manage().window().maximize();
        // Навигация на https://test-stand.gb.ru/login
        driver.get("https://test-stand.gb.ru/login");
        // Объект созданного Page Object
//        gb.LoginPage loginPage = new gb.LoginPage(driver, wait);
   }

    @Test
    void emptyLoginAndPassword (){
        // Логин в систему с пустыми полями логина и пароля
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.emptyInput();
        assertEquals(loginPage.getErrorText(), "401");
    }

    @Test
    public void testAddingGroupOnMainPage() {
        checkLogin();
        // Создание группы. Даём ей уникальное имя, чтобы в каждом запуске была проверка нового имени
        String groupTestName = "New Test Group " + System.currentTimeMillis();
        MainPage mainPage = new MainPage(driver, wait);
        mainPage.createGroup(groupTestName);
    }

    @Test
    void testArchiveGroupOnMainPage() {
        checkLogin();
        String groupTestName = "New Test Group " + System.currentTimeMillis();
        MainPage mainPage = new MainPage(driver, wait);
        mainPage.createGroup(groupTestName);
        // Требуется закрыть модальное окно
        mainPage.closeCreateGroupModalWindow();
        // Изменение созданной группы с проверками
        assertEquals("active", mainPage.getStatusOfGroupWithTitle(groupTestName));
        mainPage.clickTrashIconOnGroupWithTitle(groupTestName);
        assertEquals("inactive", mainPage.getStatusOfGroupWithTitle(groupTestName));
        mainPage.clickRestoreFromTrashIconOnGroupWithTitle(groupTestName);
        assertEquals("active", mainPage.getStatusOfGroupWithTitle(groupTestName));
    }

    private void checkLogin() {
        // Логин в систему с помощью метода из класса Page Object
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.login(USERNAME, PASSWORD);
        // Инициализация объекта класса gb.MainPage
        MainPage mainPage = new MainPage(driver, wait);
        mainPage = new MainPage(driver, wait);
        // Проверка, что логин прошёл успешно
        Assertions.assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
    }

    private void makeGroup(String groupTestName){

        MainPage mainPage = new MainPage(driver, wait);
        mainPage.createGroup(groupTestName);
        mainPage.closeCreateGroupModalWindow();
    }

    @Test
    void changeStudentsNumber() throws InterruptedException {
        checkLogin();
        String groupTestName = "NewGroup" + System.currentTimeMillis();
        makeGroup(groupTestName);
        MainPage mainPage = new MainPage(driver, wait);
        mainPage.clickPlusStudentBtn(groupTestName);
        mainPage.addStudent(number);
        mainPage.clickSaveBtnOnCreateStudentsForm();
        mainPage.closeCreateStudentsModalWindow();
        mainPage.waitStudentCount(groupTestName, number);
        mainPage.clickZoomIconOnGroupWithTitle(groupTestName);
        String firstStudent = mainPage.getNameByIndex(0);
        assertEquals("active", mainPage.getStatusOfStudent(firstStudent));
        mainPage.clickTrashIconOnStudentWithName(firstStudent);
        assertEquals("block", mainPage.getStatusOfStudent(firstStudent));
        mainPage.clickRestoreFromTrashIconOnStudentWithName(firstStudent);
        assertEquals("active", mainPage.getStatusOfStudent(firstStudent));
}



    @AfterEach
    public void teardown() {
        // Закрываем все окна брайзера и процесс драйвера
        driver.quit();
    }

}
