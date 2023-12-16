package gb;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import gb.elements.GroupTableRow;
import gb.elements.StudentTableRow;
import org.openqa.selenium.support.FindBy;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {
    @FindBy(css = "nav li.mdc-menu-surface--anchor a")
    private SelenideElement usernameLinkInNavBar;
    @FindBy(xpath = "//nav//li[contains(@class,'mdc-menu-surface--anchor')]//span[text()='Profile']")
    private SelenideElement profileLinkInNavBar;
    @FindBy(id = "create-btn")
    private SelenideElement createGroupButton;
    @FindBy(xpath = "//form//span[contains(text(), 'Group name')]/following-sibling::input")
    private SelenideElement groupNameField;
    @FindBy(css = "form div.submit button")
    private SelenideElement submitButtonOnModalWindow;
    @FindBy(xpath = "//span[text()='Creating Study Group']" +
            "//ancestor::div[contains(@class, 'form-modal-header')]//button")
    private SelenideElement closeCreateGroupIcon;
    @FindBy(xpath = "//table[@aria-label='Tutors list']/tbody/tr")
    private ElementsCollection rowsInGroupTable;
    @FindBy(css = "div#generateStudentsForm-content input")
    private SelenideElement createStudentsForm;
    @FindBy(css = "div#generateStudentsForm-content div.submit button")
    private SelenideElement saveCreateStudentsForm;
    @FindBy(xpath = "//h2[@id='generateStudentsForm-title']/../button")
    private SelenideElement closeCreateStudentsForm;
    private final ElementsCollection rowsInStudentTable = $$x("//table[@aria-label='User list']/tbody/tr");

    // Group Table

    public SelenideElement waitAndGetGroupTitleByText(String title) {
        return $x(String.format("//table[@aria-label='Tutors list']/tbody//td[text()='%s']", title)).shouldBe(visible);
    }

    public void createGroup(String groupName) {
        createGroupButton.shouldBe(Condition.visible).click();
        groupNameField.shouldBe(visible).setValue(groupName);
        submitButtonOnModalWindow.shouldBe(visible).click();
        waitAndGetGroupTitleByText(groupName);
    }

    public void closeCreateGroupModalWindow() {
        closeCreateGroupIcon.shouldBe(visible).click();
    }

    public String getUsernameLabelText() {
        return usernameLinkInNavBar.shouldBe(visible).getText().replace("\n", " ");
    }

    public void clickTrashIconOnGroupWithTitle(String title) {
        getRowByTitle(title).clickTrashIcon();
    }

    public void clickRestoreFromTrashIconOnGroupWithTitle(String title) {
        getRowByTitle(title).clickRestoreFromTrashIcon();
    }

    public String getStatusOfGroupWithTitle(String title) {
        return getRowByTitle(title).getStatus();
    }

    private GroupTableRow getRowByTitle(String title) {
        return rowsInGroupTable.shouldHave(sizeGreaterThan(0))
                .asDynamicIterable().stream()
                .map(GroupTableRow::new)
                .filter(row -> row.getTitle().equals(title))
                .findFirst().orElseThrow();
    }

    public void waitStudentCount(String groupTestName, int number) {
        getRowByTitle(groupTestName).waitCountStudents(number);
    }

    public void clickUsernameLabel() {
        usernameLinkInNavBar.shouldBe(visible).click();
    }

    public void clickProfileLink() {
        profileLinkInNavBar.shouldBe(visible).click();
    }

    //Student Table

    void clickPlusStudentBtn(String title){
        getRowByTitle(title).clickAddStudentIcon();
    }

    public void clickZoomIconOnGroupWithTitle(String title){
        getRowByTitle(title).clickZoomIcon();
    }

    public void addStudent(int amount){
        createStudentsForm.shouldBe(visible).setValue(String.valueOf(amount));
    }

    public void clickSaveBtnOnCreateStudentsForm(){
        saveCreateStudentsForm.shouldBe(visible).click();
    }

    public void closeCreateStudentsModalWindow(){
        closeCreateStudentsForm.click();
    }

    public void clickTrashIconOnStudentWithName(String name) {
        getStudentRowName(name).clickTrashIcon();
    }

    public void clickRestoreFromTrashIconOnStudentWithName(String name) {
        getStudentRowName(name).clickRestoreFromTrashIcon();
    }

    public String getStatusOfStudent(String name){
        return getStudentRowName(name).getStatus();
    }


    public String getNameByIndex(int index) {
        return rowsInStudentTable.shouldHave(sizeGreaterThan(0))
                .asDynamicIterable().stream()
                .map(StudentTableRow::new)
                .toList().get(index).getName();
    }

    private StudentTableRow getStudentRowName(String name) {
        return rowsInStudentTable.shouldHave(sizeGreaterThan(0))
                .asDynamicIterable().stream()
                .map(StudentTableRow::new)
                .filter(row -> row.getName().equals(name))
                .findFirst().orElseThrow();
    }
}
