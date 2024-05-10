package ru.urfu.controller.constant;

/**
 * Список текстов сообщений отправляемых пользователю при вызове исключения
 */
public enum ErrorMessage {

    /**
     * Текст сообщения отправляемого пользователю при вызове {@link ru.urfu.exceptions.ApiNotFoundException}
     */
    API_NOT_FOUND_EXCEPTION("API не поддерживается"),

    /**
     * Текст сообщения отправляемого пользователю при вызове {@link ru.urfu.exceptions.ApiNotSupportedCurrencyException}
     */
    CURRENCY_NOT_FOUND_EXCEPTION("API не поддерживает валюту"),

    /**
     * Текст сообщения отправляемого пользователю при вызове {@link ru.urfu.exceptions.SendRequestException}
     */
    SEND_REQUEST_EXCEPTION("Ошибка обращения к API"),

    /**
     * Текст сообщения отправляемого пользователю при вызове {@link ru.urfu.exceptions.ParseJsonException}
     */
    PARSE_JSON_EXCEPTION("Ошибка получения стоимости"),

    /**
     * Текст сообщения отправляемого пользователю при вызове {@link ru.urfu.exceptions.CallbackException}
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
