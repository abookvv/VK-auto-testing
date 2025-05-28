package org.example.pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.FindBy;

public class HomePage {
    @FindBy(xpath = "//h1[@class='title']")
    private SelenideElement welcomeMessage;

    public String getWelcomeText() {
        return welcomeMessage.getText();
    }
}