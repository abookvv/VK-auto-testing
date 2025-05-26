package org.example.tests;

import java.time.Duration;
import com.codeborne.selenide.Configuration;
import org.example.pages.ExamplePage;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;

import static com.codeborne.selenide.Selenide.title;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExamplePageTest {
    private ExamplePage page;
    @BeforeEach
    void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Anne\\chromedriver-win64\\chromedriver.exe");
        Configuration.browser = "chrome";
        Configuration.timeout = 120000;
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

        String articleTitle = page.getArticleTitle();
        assertTrue(articleTitle.toLowerCase().contains(searchQuery.toLowerCase()),
                "Заголовок статьи не содержит искомый текст: " + searchQuery);
    }
    @Test
    public void testLoginButtonVisible() {
        assertTrue(page.isLoginButtonVisible());
    }

    @Test
    public void testNavigationToNewsSection() {
        page.clickNewsSection();
        WebDriverWait wait = new WebDriverWait(WebDriverRunner.getWebDriver(), Duration.ofSeconds(30));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Новости']")));
        } catch (TimeoutException e) {
            System.out.println("Элемент не появился на странице в течение 30 секунд");
        }
        assertTrue(WebDriverRunner.url().contains("/news") || WebDriverRunner.url().contains("/ru/news"),
                "URL не содержит ожидаемый путь /news, текущий URL: " + WebDriverRunner.url());}
}
