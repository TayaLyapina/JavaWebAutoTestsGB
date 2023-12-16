package gb;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private SelenideElement usernameField = $("form#login input[type='text']");
    private SelenideElement passwordField = $("form#login input[type='password']");
    private SelenideElement loginButton = $("form#login button");
    private SelenideElement errorBlock = $("div.error-block");

    public void login(String username, String password) {
        typeUsernameInField(username);
        typePasswordInField(password);
        clickLoginButton();
    }

    public void typeUsernameInField(String username) {
        usernameField.shouldBe(visible).setValue(username);
    }

    public void typePasswordInField(String password) {
        passwordField.shouldBe(visible).setValue(password);
    }

    public void clickLoginButton() {
        loginButton.shouldBe(visible).click();
    }

    public void emptyInput(){
        clickLoginButton();
    }

    public String getErrorText() {
        return errorBlock.should(visible).text().replace("\n", " ");
    }

}
