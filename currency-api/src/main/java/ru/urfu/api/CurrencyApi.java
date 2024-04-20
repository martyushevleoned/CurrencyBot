package ru.urfu.api;

import ru.urfu.model.CurrencyResponse;

import java.util.Set;

/**
 * Класс позволяющий обращаться получать стоимость валют
 */
public interface CurrencyApi {

    /**
     * Возвращает название API
     */
    String getDescription();

    /**
     * Возвращает список валют стоимость которых можно получить при помощи данного API
     */
    Set<String> getCurrencies();

    /**
     * Возвращает актуальную стоимость валюты
     */
    CurrencyResponse getPrice(String currency);
}
