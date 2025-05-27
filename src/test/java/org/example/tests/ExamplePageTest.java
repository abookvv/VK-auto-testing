package org.example.tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.example.pages.ExamplePage;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExamplePageTest {
    private ExamplePage page;
    private static final Logger logger = LoggerFactory.getLogger(ExamplePageTest.class);

    @BeforeAll
    public void setUpClass() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Anne\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito"); // Запуск в режиме инкогнито
        Configuration.browserCapabilities = options;
        Configuration.browser = "chrome";
        Configuration.timeout = 120000;
    }
    @BeforeEach
    public void setUp() {
        page = new ExamplePage();
        page.openPage();
    }
    @AfterAll
    public void tearDownClass() {
        System.out.println("Все тесты завершены.");
        closeWebDriver();
    }

    @Test
    @Tag("smoke")
    @DisplayName("Проверка заголовка главной страницы")
    public void testMainPageTitle() {
        Configuration.timeout = 150000;
        assertTrue(title().contains("Хабр"));
    }

    @Test
    @DisplayName("Поиск статьи")
    public void testSearchArticle() {
        Configuration.pageLoadTimeout = 120000; // Увеличьте до 120 секунд
        String searchQuery = "Java";
        logger.info("Тест: Поиск статьи начат, запрос: {}", searchQuery);
        logger.info("Ожидание и клик по кнопке открытия поиска");
        page.openSearchButton.shouldBe(Condition.visible).click();
        logger.info("Ввод текста поиска: {}", searchQuery);
        page.searchInput.shouldBe(Condition.visible).setValue(searchQuery);
        logger.info("Клик по кнопке поиска");
        page.searchButton.shouldBe(Condition.visible).click();
        logger.info("Ожидание и клик по первой статье");
        page.firstArticleLink.shouldBe(Condition.visible).click();

        String articleTitle = page.getArticleTitle();
        assertTrue(articleTitle.toLowerCase().contains(searchQuery.toLowerCase()),
                "Заголовок статьи не содержит искомый текст: " + searchQuery);
    }

    @Test
    @DisplayName("Проверка кнопки 'Войти'")
    public void testLoginButtonVisible() {
        assertTrue(page.isLoginButtonVisible());
    }

    @Test
    @Disabled("Тест временно отключен из-за изменений на странице")
    @DisplayName("Переход в раздел 'Новости'")
    public void testNavigationToNewsSection() {
        page.clickNewsSection();
        String currentUrl = WebDriverRunner.url();
        assertTrue(currentUrl.contains("/news") || currentUrl.contains("/ru/news"),
                "URL не содержит ожидаемый путь /news, текущий URL: " + currentUrl);
    }

    @Test
    @DisplayName("Переход по кнопке 'Разработка'")
    public void testToDevelopArticles() {
        page.clickDevelopArticles();
        // Используем assertAll для группировки утверждений
        assertAll("Проверка перехода по кнопке 'Разработка'",
                () -> $("h1").shouldHave(Condition.text("Разработка"), Duration.ofSeconds(10)),
                () -> {
                    String currentUrl = WebDriverRunner.url();
                    assertTrue(currentUrl.contains("ru/flows/develop/articles/") || currentUrl.contains("develop/articles"),
                            "URL не содержит ожидаемый путь /flows/develop/articles/, текущий URL: " + currentUrl);
                }
        );
    }
}