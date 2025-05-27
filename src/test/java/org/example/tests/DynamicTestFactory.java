package org.example.tests;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DynamicTestFactory {
    static {
        WebDriverManager.chromedriver()
                .driverVersion("136.0.7103.94")
                .forceDownload()  // Принудительно скачать нужную версию
                .setup();
    }
    @TestFactory
    Stream<DynamicTest> dynamicSelenideTests() {
        Collection<String> urls = Arrays.asList(
                "https://habr.com/ru/",
                "https://habr.com/ru/news/",
                "https://habr.com/ru/flows/develop/articles/"
        );
        return urls.stream()
                .map(url -> DynamicTest.dynamicTest("Проверка доступности " + url, () -> {
                    // Открываем страницу для каждого динамического теста
                    Selenide.open(url);
                    // Проверяем, что заголовок <h1> видим
                    Selenide.$("h1").shouldBe(Condition.visible, Duration.ofSeconds(10));
                    // Получаем текущий URL
                    String currentUrl = WebDriverRunner.url();
                    // Проверяем, что заголовок страницы не пустой
                    String title = Selenide.title();
                    assertTrue(title != null && !title.isEmpty(), "Заголовок страницы пуст для URL: " + url);
                    // Проверяем, что текущий URL содержит часть ожидаемого пути
                    String expectedPath = url.replace("https://", "").replace("http://", "");
                    assertTrue(currentUrl.contains(expectedPath) || currentUrl.contains(expectedPath.substring(expectedPath.indexOf('/'))),
                            "URL страницы (" + currentUrl + ") не содержит ожидаемый путь: " + expectedPath);
                }));
    }
}
