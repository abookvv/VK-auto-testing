package org.example.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.example.pages.PageFactory;
import org.example.pages.HomePage;
import org.example.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    private final String TEST_EMAIL = "anna301488@gmail.com";
    private final String TEST_PASSWORD = "anna301488";

    @Test
    @DisplayName("Успешный вход с валидными данными")
    public void loginTest() {
        Configuration.timeout = 20_000;
        open("https://habr.com/ru/feed/");
        LoginPage loginPage = page(LoginPage.class);
        loginPage.enterEmail(TEST_EMAIL)
                .enterPassword(TEST_PASSWORD)
                .submit();
    }
//        loginPage.errorMessage
//                .shouldBe(visible)
//                .shouldHave(text("Неверный email или пароль"));
}