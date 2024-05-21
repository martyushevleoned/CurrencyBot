package ru.urfu.exceptions;

/**
 * Ошибка создания {@link ru.urfu.utils.callback.Callback}
 */
public class CallbackException extends RuntimeException {
    public CallbackException(String message) {
        super(message);
    }
}
