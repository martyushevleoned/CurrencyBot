package ru.urfu.exceptions;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

/**
 * Ошибка парсинга {@link CallbackQuery}
 */
public class CallbackException extends RuntimeException {
    public CallbackException(String message) {
        super(message);
    }
}
