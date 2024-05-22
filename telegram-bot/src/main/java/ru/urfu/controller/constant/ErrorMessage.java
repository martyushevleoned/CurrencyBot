package ru.urfu.controller.constant;

import ru.urfu.exceptions.*;

/**
 * Тексты сообщений, отправляемых пользователю при возникновении исключений
 */
public enum ErrorMessage {

    /**
     * Текст сообщения, отправляемого пользователю при вызове исключения в методе {@link ru.urfu.ApiService#getDescription}
     */
    API_NOT_FOUND_EXCEPTION("Не удалось обратиться к API"),

    /**
     * Текст сообщения, отправляемого пользователю при вызове исключения в методе {@link ru.urfu.ApiService#getPrice}
     */
    GET_PRICE_EXCEPTION("Не удалось получить стоимости валюты"),

    /**
     * Текст сообщения, отправляемого пользователю при вызове {@link CallbackException}
     */
    CALLBACK_EXCEPTION("Ошибка создания кнопки");

    /**
     * Текст сообщения ошибки
     */
    private final String text;

    ErrorMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
