package org.example.wrapper;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

public class ClickableElement {
    private final SelenideElement element;

    public ClickableElement(SelenideElement element) {
        this.element = element;
    }

    public void clickWithWait(Duration timeout) {
        element.shouldBe(Condition.visible, timeout).click();
    }


    public String getText() {
        return element.shouldBe(Condition.visible).getText();
    }
}
