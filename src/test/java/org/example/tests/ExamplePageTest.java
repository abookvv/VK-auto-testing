package org.example.tests;

import com.codeborne.selenide.Configuration;
import org.example.pages.ExamplePage;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static com.codeborne.selenide.Selenide.title;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExamplePageTest {
    private ExamplePage page;
    @BeforeEach
    void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Anne\\chromedriver-win64\\chromedriver.exe"); // Замените на фактический путь
        Configuration.browser = "chrome"; // Убедитесь, что браузер установлен на Chrome
//        WebDriver driver = new ChromeDriver();
        page = new ExamplePage();
        page.openPage();
    }
    @Test
    public void testMainPageTitle() {
        assertTrue(title().contains("Хабр"));
    }
    @Test
    public void testSearchArticle() {
        String searchQuery = "Java";
        page.searchFor(searchQuery);
        page.openFirstArticle();
        assertTrue(page.getArticleTitle().toLowerCase().contains(searchQuery.toLowerCase()));
    }
    @Test
    public void testLoginButtonVisible() {
        assertTrue(page.isLoginButtonVisible());
    }
}
