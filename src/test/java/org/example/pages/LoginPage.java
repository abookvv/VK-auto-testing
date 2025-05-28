package org.example.pages;  // Важно: пакет соответствует расположению файла

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class LoginPage {

    private SelenideElement emailField = $x("//input[@type='email']");
    private SelenideElement passwordField = $x("//input[@type='password']");
    private SelenideElement submitButton = $x("//button[@type='submit']");


    @FindBy(xpath = "//button[text()='Войти']")
    private SelenideElement loginButton;

    @FindBy(xpath = "//div[@role='checkbox']")
    private SelenideElement captchaIframe;

    @FindBy(xpath = "//img[contains(@class,'tm-entity-image__pic')]") // Локатор аватара
    private SelenideElement userAvatar;

    public void handleCaptcha() {
        if (captchaIframe.exists()) {
            // Варианты обработки капчи
            solveCaptchaManually(); // Ручной ввод
            // или bypassCaptchaForTesting(); // Обход для тестового окружения
        }
    }

    private void solveCaptchaManually() {
        System.out.println("=== ВНИМАНИЕ: Требуется ручной ввод капчи ===");
        System.out.println("1. Введите капчу в появившееся окно");
        System.out.println("2. Нажмите Enter в консоли после завершения");

        switchTo().frame(captchaIframe);
        try {
            // Даем время на ручной ввод
            Thread.sleep(60000); // 60 секунд на ввод
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        switchTo().defaultContent();
    }

    //Конструктор с инициализацией PageFactory
    public LoginPage() {
        PageFactory.initElements(WebDriverRunner.getWebDriver(), this); // Правильный способ
    }

    //Метод открытия страницы
    public LoginPage open() {
        Selenide.open("/login"); // Базовый URL задаётся в selenide.properties
        return this;
    }

    //Ввод email (с проверкой видимости поля)
    public LoginPage enterEmail(String email) {
        emailField
                .shouldBe(visible, Duration.ofSeconds(5))
                .setValue(email);
        return this;
    }

    //Ввод пароля
    public LoginPage enterPassword(String password) {
        passwordField
                .shouldBe(visible)
                .setValue(password);
        return this;
    }

    //Отправка формы
    public HomePage submit() {
        submitButton
                .shouldBe(enabled)
                .click();
        return new HomePage(); // Возвращаем следующую страницу
    }

    //Полный процесс входа (цепочка методов)
    public HomePage loginAs(String email, String password) {
        return enterEmail(email)
                .enterPassword(password)
                .submit();
    }

    //Проверка успешного входа
    public boolean isLoggedIn() {
        return userAvatar.exists() && userAvatar.isDisplayed();
    }

}