package gb.elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import java.time.Duration;
import static com.codeborne.selenide.Condition.visible;

public class StudentTableRow {

    private final SelenideElement root;

    public StudentTableRow(SelenideElement root){
        this.root = root;
    }

    public String getName() {
        return root.$x("./td[2]").shouldBe(Condition.visible).getText();
//        return root.findElement(By.xpath("./td[2]")).getText();
    }

    public String getStatus() {
        return root.$x("./td[4]").shouldBe(Condition.visible).getText();

    }

    public void clickTrashIcon() {
        root.$x("./td/button[text()='delete']").shouldBe(Condition.visible).click();
        root.$x("./td/button[text()='restore_from_trash']").should(visible, Duration.ofSeconds(30));
    }

    public void clickRestoreFromTrashIcon() {
        root.$x("./td/button[text()='restore_from_trash']").should(Condition.visible).click();
        root.$x("./td/button[text()='delete']").shouldBe(visible, Duration.ofSeconds(30));
    }


}
