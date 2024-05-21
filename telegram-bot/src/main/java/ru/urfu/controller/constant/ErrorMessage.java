package ru.urfu.controller.constant;

import ru.urfu.exceptions.*;

/**
 * TODO необходимо использовать для обработки ошибок или удалить
 * Тексты сообщений, отправляемых пользователю при возникновении исключений
 */
public enum ErrorMessage {

    /**
     * Текст сообщения, отправляемого пользователю при вызове {@link ApiNotFoundException}
     */
    API_NOT_FOUND_EXCEPTION("API не поддерживается"),

    /**
     * Текст сообщения, отправляемого пользователю при вызове {@link ApiNotSupportedCurrencyException}
     */
    CURRENCY_NOT_FOUND_EXCEPTION("API не поддерживает валюту"),

    /**
     * Текст сообщения, отправляемого пользователю при вызове {@link SendRequestException}
     */
    SEND_REQUEST_EXCEPTION("Ошибка обращения к API"),

    /**
     * Текст сообщения, отправляемого пользователю при вызове {@link ParseJsonException}
     */
    PARSE_JSON_EXCEPTION("Ошибка получения стоимости"),

    /**
     * Текст сообщения, отправляемого пользователю при вызове {@link CallbackException}
     */
    CALLBACK_EXCEPTION("Ошибка обработки нажатия кнопки");

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
