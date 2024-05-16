package ru.urfu.utils.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

/**
 * Список всех опций использующихся в {@link Callback}
 */
enum Option {

    /**
     * Опция, для указания названия меню
     */
    MENU_NAME("MN"),

    /**
     * Опция, для указания названия API
     */
    API_NAME("AP"),

    /**
     * Опция, для указания названия валюты
     */
    CURRENCY_NAME("CU"),

    /**
     * Опция, для добавления валюты в список "отслеживаемых"
     */
    ADD_TO_TRACK("AD"),

    /**
     * Опция, для удаления валюты из списка "отслеживаемых"
     */
    REMOVE_FROM_TRACK("RE"),

    /**
     * Опция, для указания номера страницы в многостраничном меню
     */
    PAGE_INDEX("PI"),

    /**
     * Опция, определяющая наличие чего-либо
     */
    CONTAINS("CO");

    /**
     * Уникальное сокращение опции,
     * во избежание превышения допустимого размера
     * {@link CallbackQuery#getData}
     */
    private final String option;

    Option(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }
}
