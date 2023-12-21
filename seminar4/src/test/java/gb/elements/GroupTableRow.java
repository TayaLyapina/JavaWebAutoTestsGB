package gb.elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import java.time.Duration;
import static com.codeborne.selenide.Condition.visible;

public class GroupTableRow {

    private final SelenideElement root;

    public GroupTableRow(SelenideElement root) {
        this.root = root;
    }

    public String getTitle() {
        return root.$x("./td[2]").should(Condition.visible).getText();
    }

    public String getStatus() {
        return root.$x("./td[3]").should(Condition.visible).getText();
    }

    public void clickTrashIcon() {
        root.$x("./td/button[text()='delete']").should(Condition.visible).click();
        root.$x("./td/button[text()='restore_from_trash']").should(Condition.visible);
    }

    public void clickRestoreFromTrashIcon() {
        root.$x("./td/button[text()='restore_from_trash']").should(Condition.visible).click();
        root.$x("./td/button[text()='delete']").should(visible, Duration.ofSeconds(30));
    }


    public void clickAddStudentIcon() {
        root.$("td button i.material-icons").should(Condition.visible).click();
    }

    public void clickZoomIcon() {
        root.$x(".//td/button[contains(., 'zoom_in')]").should(Condition.visible).click();
    }

    public void waitCountStudents(int expectedCount) {
        root.$x("./td[4]//span[text()='%s']".formatted(expectedCount)).should(visible, Duration.ofSeconds(30));
    }

}
