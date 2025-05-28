package org.example.tests;

import com.codeborne.selenide.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.models.Article;
import org.example.pages.ExamplePage;
import org.example.wrapper.ClickableElement;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExamplePageTest {
    private ExamplePage page;
    private static final Logger logger = LoggerFactory.getLogger(ExamplePageTest.class);

    @BeforeAll
    public void setUpClass() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Anne\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        Configuration.browserCapabilities = options;
        Configuration.browser = "chrome";
        Configuration.timeout = 15000;
    }

    @BeforeEach
    public void setUp() {
        Configuration.pageLoadTimeout = 60000; // Увеличиваем таймаут загрузки
        open("https://habr.com/ru/");
        $("body").shouldBe(Condition.visible); // Явное ожидание
        page = new ExamplePage();
        logger.info("Инициализирована тестовая страница");
    }

    @AfterAll
    public void tearDownClass() {
        closeWebDriver();
        logger.info("Все тесты завершены, браузер закрыт");
    }

    @Test
    @Tag("smoke")
    @DisplayName("Проверка заголовка главной страницы")
    public void testMainPageTitle() {
        assertTrue(title().contains("Хабр"));
        logger.info("Заголовок страницы содержит 'Хабр'");
    }

    @Test
    @DisplayName("Работа с объектом Article")
    public void testArticleObject() {
        Article article = page.searchFor("Java")
                .openFirstArticle()
                .getCurrentArticle();

        assertNotNull(article, "Article не должен быть null");
        assertTrue(article.containsText("Java"));
        logger.info("Проверка Article успешна: {}", article);
    }

    @ParameterizedTest
    @MethodSource("searchTestData")
    @DisplayName("Параметризованный поиск")
    void testSearchArticles(String query, boolean shouldContain) {
        page.searchFor(query);

        if (shouldContain) {
            // Для существующих запросов
            $$("article.tm-articles-list__item").shouldBe(CollectionCondition.sizeGreaterThan(0));

            // Открываем первую статью
            page.firstArticleLink
                    .shouldBe(Condition.visible)
                    .click();

            // Ждем загрузки контента статьи
            page.description.shouldBe(Condition.visible);

            // Проверяем текст в разных частях статьи
            String title = page.articleTitle.getText().toLowerCase();
            String content = page.description.getText().toLowerCase();
            String searchTerm = query.toLowerCase();

            assertTrue(
                    title.contains(searchTerm) || content.contains(searchTerm),
                    String.format("Текст '%s' не найден ни в заголовке (%s), ни в содержании",
                            query, title.substring(0, 30) + "...")
            );

        } else {
            // Для несуществующих запросов
            page.emptyPage
                    .shouldBe(Condition.visible)
                    .shouldHave(Condition.text("К сожалению, здесь пока нет ни одной публикации"));
        }

        logger.info("Тест для запроса '{}' завершен успешно", query);
    }


    private static Stream<Arguments> searchTestData() {
        return Stream.of(
                Arguments.of("Java", true),
                Arguments.of("Python", true),
                Arguments.of("Selenium", true),
                Arguments.of("NonexistentWord123", false)
        );
    }

    @Test
    @DisplayName("Комплексный тест с паттернами")
    @Disabled
    public void testSearchArticleWithPatterns() {
        // Page Object + Chain of Invocations
        String searchQuery = "Java";
        page.searchFor(searchQuery).ensureResultsExist(searchQuery);

        page.openFirstArticle();

        Article article = page.toArticleModel();
        logger.info("Получена статья: {}", article);


        String titleData = article.getTitle().toLowerCase();
        String contentData = article.getContent().toLowerCase();
        String searchTerm = searchQuery.toLowerCase();

        assertTrue(
                titleData.contains(searchTerm) || contentData.contains(searchTerm),
                String.format("Текст '%s' не найден ни в заголовке (%s), ни в содержании",
                        searchQuery, titleData.substring(0, Math.min(titleData.length(), 30)) + "...")
        );

        new ClickableElement(page.getLoginButton())
                .clickWithWait(Duration.ofSeconds(5));

        // 5. Комплексная проверка
        assertAll(
                () -> assertTrue(article.containsText(searchQuery),
                        "Текст '" + searchQuery + "' не найден в статье"),
                () -> assertTrue(page.isLoginButtonVisible()),
                () -> assertTrue(WebDriverRunner.url().contains("habr.com"))
        );
    }
}