package ru.urfu.model;

import ru.urfu.ApiService;

import java.time.Instant;

/**
 * Ответ от {@link ApiService ApiManager}, содержащий стоимость валюты и время обращения к API
 */
public record CurrencyResponse(double price, Instant instant) {
}
