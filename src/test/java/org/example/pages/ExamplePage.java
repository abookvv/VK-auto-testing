package org.example.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.ex.ElementNotFound;
import org.example.models.Article;
import org.openqa.selenium.support.ui.LoadableComponent;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExamplePage extends LoadableComponent<ExamplePage> {
    public SelenideElement searchInput = $("input[name='q']");
    public SelenideElement openSearchButton = $x("//a[contains(@class, 'tm-header-user-menu__item')]");
    public SelenideElement firstArticleLink = $("article.tm-articles-list__item a.tm-title__link");
    public SelenideElement articleTitle = $("h2.tm-title");
    private SelenideElement loginButton = $x("//button[text()='Войти']");
    private SelenideElement newsSection = $x("//a[text()='Новости']");
    private SelenideElement developArticles = $x("//a[text()='Разработка']");
    public SelenideElement description = $("div.tm-article-body");
    public SelenideElement articleContent = $("div.tm-article-body");
    public SelenideElement clickOnSearch = $x("//span[@class='tm-svg-icon__wrapper tm-search__icon']");
    public SelenideElement emptyPage = $("div.tm-empty-placeholder__text");
    public SelenideElement textik = $("div.tm-empty-state__text");
    public SelenideElement textt = $("div.tm-empty-state");
    public SelenideElement articleText = $("div.tm-articles-list");


    @Override
    protected void load() {
        open("https://habr.com/ru/");
    }

    @Override
    protected void isLoaded() throws Error {
        // Проверяем ключевые элементы
        openSearchButton.shouldBe(Condition.visible);
        loginButton.shouldBe(Condition.visible);
        assertTrue(url().contains("habr.com"));
    }

    // Chain of invocations
    public ExamplePage searchFor(String query) {
        openSearchButton.shouldBe(Condition.visible).click();
        searchInput.shouldBe(Condition.visible).setValue(query);
        clickOnSearch.shouldBe(Condition.visible).click();
        return this;
    }

    public ExamplePage openFirstArticle() {
        firstArticleLink.shouldBe(Condition.visible).click();
        return this;
    }

    public String getArticleTitle() {
        return articleTitle.shouldBe(Condition.visible, Duration.ofSeconds(10))
                .getText();
    }

    public boolean isLoginButtonVisible() {
        return loginButton.isDisplayed();
    }

    public ExamplePage clickDevelopArticles() {
        developArticles.shouldBe(Condition.visible).click();
        return this;
    }


    public ExamplePage clickNewsSection() {
        newsSection.shouldBe(Condition.visible).click();
        return this;
    }

    public Article getCurrentArticle() {
        return new Article(
                articleTitle.shouldBe(Condition.visible).getText(),
                url(),
                articleContent.shouldBe(Condition.visible).getText()
        );
    }

    public SelenideElement getLoginButton() {
        return loginButton.shouldBe(Condition.visible);
    }

    public Article toArticleModel() {

        String title = articleTitle.shouldBe(Condition.visible).getText();
        String content = description.shouldBe(Condition.visible).getText();
        return new Article(title, WebDriverRunner.url(), content);
    }

    public ExamplePage ensureResultsExist(String searchQuery) {
        articleText.shouldBe(Condition.visible, Duration.ofSeconds(10));
        if (textt.is(Condition.visible)) {
            String message = textik.shouldBe(Condition.visible).getText();
            throw new IllegalStateException("По запросу '" + searchQuery + "' нет результатов: " + message);
        }

        // 3. Проверяем, что есть хотя бы одна статья
        $$("article.tm-articles-list__item").shouldBe(CollectionCondition.sizeGreaterThan(0));

        return this;
    }

}

