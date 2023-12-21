package gb;
;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

import java.io.File;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage {

    @FindBy(xpath = "//h3/following-sibling::div" +
            "//div[contains(text(), 'Full name')]/following-sibling::div")
    private SelenideElement fullNameInAdditionalInfo;
    @FindBy(css = "div.mdc-card h2")
    private SelenideElement fullNameInAvatarSection;
    private final SelenideElement editButton = $("button[title='More options']");
    private SelenideElement form = $("form#update-item");
    private SelenideElement inputAvatar = form.$("input[type='file']");
    private SelenideElement birthdayField = form.$("input[type='date']");
    private SelenideElement saveFormBtn = form.$("button[type='submit']");
    private SelenideElement fullBirthday = $x("//h3/following-sibling::div//div[contains(text(), 'Date of birth')]/following-sibling::div");


    public String getFullNameFromAdditionalInfo() {
        return fullNameInAdditionalInfo.shouldBe(visible).text();
    }

    public String getFullNameFromAvatarSection() {
        return fullNameInAvatarSection.shouldBe(visible).text();
    }

    public void editButtonClick(){
        editButton.shouldBe(visible).click();
    }

    public void uploadNewAvatar(File file){
        form.shouldBe(visible).uploadFile(file);
    }

    public String getAvatarValue(){
        String inputValue = inputAvatar.should(visible).getValue();
        return Objects.requireNonNull(inputValue)
                .substring(inputValue.lastIndexOf("\\") + 1);
    }

    public void inputBirthdayDate(String date){
        birthdayField.shouldBe(visible).setValue(date);
        saveFormBtn.shouldBe(visible).click();
    }

    public String getBirthdayText() {
        return fullBirthday.should(visible).text();
    }
}
