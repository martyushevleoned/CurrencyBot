package ru.urfu.exceptions;

/**
 * Не найден API
 */
public class ApiNotFoundException extends RuntimeException {
    public ApiNotFoundException(String message) {
        super(message);
    }
}
