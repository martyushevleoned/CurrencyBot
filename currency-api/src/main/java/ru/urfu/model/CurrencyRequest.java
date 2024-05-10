package ru.urfu.model;

import ru.urfu.ApiService;

/**
 * Класс для обращения к {@link ApiService ApiManager}, содержащий название API и название валюты
 */
public record CurrencyRequest(String api, String currency) {
}
