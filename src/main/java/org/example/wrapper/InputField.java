package org.example.wrapper;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

public class InputField {
    private final SelenideElement element;

    public InputField(SelenideElement element) {
        this.element = element;
    }

    public void setValueWithValidation(String value) {
        element.setValue(value)
                .shouldHave(Condition.value(value));
    }
}
