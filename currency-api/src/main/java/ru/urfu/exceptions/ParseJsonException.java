package ru.urfu.exceptions;

/**
 * Ошибка парсинга json
 */
public class ParseJsonException extends RuntimeException {
    public ParseJsonException(String message) {
        super(message);
    }
}
