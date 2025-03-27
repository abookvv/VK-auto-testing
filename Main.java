package org.example;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Main {
    private final String LOGIN = "your_login@example.com";
    private final String PASSWORD = "your_password";

    @Test
    public void loginTest() {
        UserPage userPage = LoginPage.open()
                .login(LOGIN, PASSWORD);

        assertTrue(userPage.isFeedDisplayed());
        assertEquals(LOGIN, userPage.getUserName());
    }
}

class LoginPage {
    private final SelenideElement loginField = $("//input[@name='email']");
    private final SelenideElement passwordField = $("//input[@name='password']");
    private final SelenideElement loginButton = $("//input[@value='Войти']");

    public static LoginPage open() {
        Selenide.open("https://ok.ru");
        return new LoginPage();
    }

    public UserPage login(String login, String password) {
        loginField.setValue(login);
        passwordField.setValue(password);
        loginButton.click();
        return new UserPage();
    }
}

class UserPage {
    private final SelenideElement feed = $("//div[contains(@class,'feed')]");
    private final SelenideElement userName = $("//div[contains(@class,'user-name')]");

    public boolean isFeedDisplayed() {
        return feed.shouldBe(visible).isDisplayed();
    }

    public String getUserName() {
        return userName.shouldBe(visible).getText();
    }
}