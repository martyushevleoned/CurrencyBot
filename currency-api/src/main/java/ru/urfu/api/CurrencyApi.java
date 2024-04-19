package ru.urfu.api;

import ru.urfu.model.Response;

import java.util.Set;

/**
 * Класс позволяющий обращаться получать стоимость валют
 */
public interface CurrencyApi {

    /**
     * Возвращает название API
     */
    String getName();

    /**
     * Возвращает список валют стоимость которых можно получить при помощи данного API
     */
    Set<String> getCurrencies();

    /**
     * Возвращает актуальную стоимость валюты
     */
    Response getPrice(String currency);
}
