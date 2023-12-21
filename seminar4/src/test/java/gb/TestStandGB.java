package gb;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;


@Epic("Тестирование сайта https://test-stand.gb.ru/")
@Feature("Вход, редактирование профиля, добавление групп и студантов в группы")
public class TestStandGB {
    private LoginPage loginPage;
    private MainPage mainPage;

    private static final String USERNAME = "Student-7";
    private static final String PASSWORD = "2e16f2452e";
    private static final String FULL_NAME = "7 Student";
    private static final String BASE_URL = "https://test-stand.gb.ru/login";

//    @BeforeAll
//    public static void selenoid(){
//        Configuration.browser = "chrome";
//        Configuration.remote = "http://localhost:4444/wd/hub";
//        Map <String, Object> map = new HashMap<>();
//        map.put("enableVNC", true);
//        map.put("enableLog", true);
//        Configuration.browserCapabilities.setCapability("selenoid:options", map);
//    }

    @BeforeEach
    public void setupTest() {
        // Навигация на https://test-stand.gb.ru/login с помощью Selenide
        open(BASE_URL);
        // Сохраняем WebDriver из Selenide
        WebDriver driver = WebDriverRunner.getWebDriver();
        // Объект созданного Page Object
        loginPage = Selenide.page(LoginPage.class);
    }

    @Test
    @DisplayName("Проверка логина с пустыми полями")
    void emptyLoginAndPassword() {
        // Логин в систему с пустыми полями логина и пароля
        loginPage.clickLoginButton();
        loginPage.emptyInput();
        assertEquals(loginPage.getErrorText(), "401 Invalid credentials.");
    }

    @Test
    @DisplayName("Добавление группы")
    public void testAddingGroupOnMainPage() {
        checkLogin();
        // Создание группы. Даём ей уникальное имя, чтобы в каждом запуске была проверка
        // нового имени
        String groupTestName = "New Test Group " + System.currentTimeMillis();
        mainPage = Selenide.page(MainPage.class);
        mainPage.createGroup(groupTestName);
        // Проверка, что группа создана и находится в таблице
        assertTrue(mainPage.waitAndGetGroupTitleByText(groupTestName).isDisplayed());

    }

    @Test
    @DisplayName("Изменение состояния группы")
    void testArchiveGroupOnMainPage() {
        checkLogin();
        String groupTestName = "New Test Group " + System.currentTimeMillis();
        mainPage = Selenide.page(MainPage.class);
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
        loginPage.login(USERNAME, PASSWORD);
        // Инициализация объекта класса gb.MainPage
        mainPage = Selenide.page(MainPage.class);
        // Проверка, что логин прошёл успешно
        Assertions.assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
    }

    private void makeGroup(String groupTestName) {
        mainPage = Selenide.page(MainPage.class);
        mainPage.createGroup(groupTestName);
        mainPage.closeCreateGroupModalWindow();
    }

    @Test
    @DisplayName("Изменение количества студантов в группе")
    void changeStudentsNumber() {
        checkLogin();
        String groupTestName = "NewGroup" + System.currentTimeMillis();
        makeGroup(groupTestName);
        mainPage = Selenide.page(MainPage.class);
        mainPage.clickPlusStudentBtn(groupTestName);
        int number = 6;
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

    @Test
    @DisplayName("Проверка имени пользователя в профиле")
    void testFullNameOnProfilePage() {
        // Логин в систему с помощью метода из класса Page Object
        loginPage.login(USERNAME, PASSWORD);
        // Инициализация объекта класса MainPage
        mainPage = Selenide.page(MainPage.class);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        // Навигация на Profile page
        mainPage.clickUsernameLabel();
        mainPage.clickProfileLink();
        // Инициализация ProfilePage с помощью Selenide
        ProfilePage profilePage = Selenide.page(ProfilePage.class);
        assertEquals(FULL_NAME, profilePage.getFullNameFromAdditionalInfo());
        assertEquals(FULL_NAME, profilePage.getFullNameFromAvatarSection());
    }

    @Test
    @DisplayName("Проверка изменения аватара пользователя")
    public void testAddingAvatar(){
        checkLogin();
        mainPage.clickUsernameLabel();
        mainPage.clickProfileLink();
        ProfilePage profilePage = Selenide.page(ProfilePage.class);
        profilePage.editButtonClick();
        assertEquals("", profilePage.getAvatarValue());
        profilePage.uploadNewAvatar(new File("src/test/resources/Avatar.PNG"));
        assertEquals("Avatar.PNG", profilePage.getAvatarValue());

    }

    @Test
    @DisplayName("Проверка изменения даты рождения пользователя")
    public void testAddingBirthdayDate(){
        checkLogin();
        mainPage.clickUsernameLabel();
        mainPage.clickProfileLink();
        ProfilePage profilePage = Selenide.page(ProfilePage.class);
        profilePage.editButtonClick();
        String date = "08071985";
        profilePage.inputBirthdayDate(date);
        assertEquals("08.07.1985", profilePage.getBirthdayText());
    }

    @AfterEach
    public void teardown() {
        // Закрываем все окна брайзера и процесс драйвера
        WebDriverRunner.closeWebDriver();
    }

}
