package org.example.pages;


import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import org.example.pages.HomePage;
import org.example.pages.LoginPage;

public class PageFactory {

    /**
     * Инициализация страницы с элементами
     * (Аналог Selenium PageFactory для Selenide)
     */
    public static <T> T initElements(Class<T> pageClass) {
        // Создаем экземпляр страницы
        T page = Selenide.page(pageClass);

        // Альтернативный вариант инициализации:
        // PageFactory.initElements(Selenide.driver(), page);

        return page;
    }

    /**
     * Специальный метод для LoginPage
     */
    public static LoginPage loginPage() {
        return initElements(LoginPage.class);
    }

    /**
     * Специальный метод для HomePage
     */
    public static HomePage homePage() {
        return initElements(HomePage.class);
    }
}