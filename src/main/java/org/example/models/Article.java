package org.example.models;  // Пакет должен соответствовать расположению

public class Article {
    private final String title;
    private final String url;
    private final String content;

    // Конструктор
    public Article(String title, String url, String content) {
        this.title = title;
        this.url = url;
        this.content = content;
    }

    // Геттеры
    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
    public String getContent() {
        return content;
    }

    public boolean containsText(String searchText) {
        String lowerSearch = searchText.toLowerCase();
        return title.toLowerCase().contains(lowerSearch) ||
                content.toLowerCase().contains(lowerSearch);
    }

    public String getShortContent(int length) {
        return content.length() > length ? content.substring(0, length) + "..." : content;
    }


    @Override
    public String toString() {
        return "Article: " + title + " | URL: " + url;
    }
}
