package ru.urfu.model;

/**
 * Класс стандартизирующий описание API
 */
public class ApiDescription {

    private final String apiName;
    private final String link;
    private final String updateTime;

    /**
     * @param link       ссылка на официальную документацию
     * @param updateTime период обновления стоимости валюты (Примеры: "1 день", "2 часа" и т.д.)
     */
    public ApiDescription(String apiName, String link, String updateTime) {
        this.apiName = apiName;
        this.link = link;
        this.updateTime = updateTime;
    }

    public String getDescription() {
        return """
                API: %s.
                Официальный сайт: %s.
                Период обновления курса: %s.
                """.formatted(apiName, link, updateTime);
    }
}
