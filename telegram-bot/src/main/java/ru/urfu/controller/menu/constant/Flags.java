package ru.urfu.controller.menu.constant;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

/**
 * Список всех флагов использующихся в {@link ru.urfu.utils.Callback}
 */
public enum Flags {

    /**
     * Флаг, хранящий название API
     */
    API_NAME_FLAG("AP"),

    /**
     * Флаг, хранящий название валюты
     */
    CURRENCY_NAME_FLAG("CU"),

    /**
     * Флаг для добавления валюты в список "отслеживаемых"
     */
    ADD_TO_TRACK_FLAG("AD"),

    /**
     * Флаг для удаления валюты из списка "отслеживаемых"
     */
    REMOVE_FROM_TRACK_FLAG("RE"),

    /**
     * Флаг, хранящий номер страницы в многостраничном меню
     */
    PAGE_INDEX_FLAG("PI"),

    /**
     * Флаг, определяющий наличие чего-либо
     */
    CONTAINS("CO");

    /**
     * Уникальное сокращение названия флага,
     * во избежание превышения допустимого размера
     * {@link CallbackQuery#getData}
     */
    private final String name;

    Flags(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
