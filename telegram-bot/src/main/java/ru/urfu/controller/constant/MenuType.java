package ru.urfu.controller.constant;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

/**
 * Тип меню
 */
public enum MenuType {

    /**
     * Главное меню
     */
    MAIN_MENU("MM","Главное меню"),

    /**
     * Меню со списком всех поддерживаемых валют
     */
    CURRENCY_ADD_TO_TRACK_LIST("CATTL","Все валюты"),

    /**
     * Меню конкретной валюты с кнопкой для добавления в "отслеживаемые"
     */
    CURRENCY_ADD_TO_TRACK("CATT",null),

    /**
     * Меню со списком всех отслеживаемых валют
     */
    TRACKED_CURRENCY_LIST("TCL","Отслеживаемые валюты"),

    /**
     * Меню конкретной отслеживаемой валюты с кнопкой для обновления курса стоимости
     */
    TRACKED_CURRENCY("TC",null),

    /**
     * Меню со списком всех поддерживаемых API
     */
    API_LIST("AL","Все API"),

    /**
     * Меню с описанием конкретного API
     */
    API("A",null);

    /**
     * Уникальное сокращение названия меню,
     * во избежание превышения допустимого размера
     * {@link CallbackQuery#getData}
     */
    private final String menuName;

    /**
     * Текст меню
     */
    private final String text;

    MenuType(String menuName, String text) {
        this.menuName = menuName;
        this.text = text;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getText() {
        return text;
    }
}
