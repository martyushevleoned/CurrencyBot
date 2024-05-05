package ru.urfu.api;

import ru.urfu.model.CurrencyResponse;

import java.util.Set;

/**
 * Позволяет обращаться к API и получать стоимость валют
 */
public interface CurrencyApi {

    /**
     * Получить название API
     */
    String getName();

    /**
     * Получить описание API
     */
    String getDescription();

    /**
     * Получить список всех валют поддерживаемых данным API
     */
    Set<String> getCurrencies();

    /**
     * Получить текущую стоимость валюты
     */
    CurrencyResponse getPrice(String currency);
}
