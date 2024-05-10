package ru.urfu.exceptions;

/**
 * Ошибка получения данных по http запросу
 */
public class SendRequestException extends RuntimeException {
    public SendRequestException(String message) {
        super(message);
    }
}
