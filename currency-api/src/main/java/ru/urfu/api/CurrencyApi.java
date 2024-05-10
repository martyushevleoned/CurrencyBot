package ru.urfu.api;

import ru.urfu.exceptions.ParseJsonException;
import ru.urfu.exceptions.SendRequestException;
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
     *
     * @throws SendRequestException если невозможно обратиться к API
     * @throws ParseJsonException   если невозможно обработать ответ API
     */
    CurrencyResponse getPrice(String currency) throws SendRequestException, ParseJsonException;
}
