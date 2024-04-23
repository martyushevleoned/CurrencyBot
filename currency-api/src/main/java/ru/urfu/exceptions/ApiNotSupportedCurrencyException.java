package ru.urfu.exceptions;

/**
 * API не поддерживает получение стоимости данной валюту
 */
public class ApiNotSupportedCurrencyException extends RuntimeException {
    public ApiNotSupportedCurrencyException(String message) {
        super(message);
    }
}
