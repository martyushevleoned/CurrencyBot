package ru.urfu.exceptions;

/**
 * Ошибка парсинга колбэка
 */
public class CallbackException extends RuntimeException {
    public CallbackException(String message) {
        super(message);
    }
}
