package ru.urfu.utils;

import ru.urfu.model.CurrencyResponse;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Кэш, хранящий стоимость валюты и время её получения.
 * Кэш инвалидирует значение, если время, которое прошло с его добавления больше, чем период обновления значения
 */
public class CurrencyCache {

    private final Duration updateDuration;
    private final Map<String, CurrencyResponse> currencyResponseMap = new HashMap<>();

    /**
     * @param updateDuration период обновления значения
     */
    public CurrencyCache(Duration updateDuration) {
        this.updateDuration = updateDuration;
    }

    /**
     * Содержится ли актуальное значение стоимости в кэше
     *
     * @param currencyName название валюты
     * @return есть ли стоимость валюты в кэше
     */
    public boolean contains(String currencyName) {
        if (!currencyResponseMap.containsKey(currencyName))
            return false;
        return Duration.between(currencyResponseMap.get(currencyName).instant(), Instant.now())
                .minus(updateDuration).isNegative();
    }

    /**
     * Отсутствует ли актуальное значение стоимости в кэше
     *
     * @param currencyName название валюты
     * @return отсутствует ли стоимость валюты в кэше
     */
    public boolean notContains(String currencyName) {
        return !contains(currencyName);
    }

    /**
     * Сохранить значение в кэш
     *
     * @param currencyName название валюты
     * @param price        стоимость валюты
     */
    public void save(String currencyName, double price) {
        currencyResponseMap.put(currencyName, new CurrencyResponse(price, Instant.now()));
    }

    /**
     * Получить значение из кэша
     *
     * @param currencyName название валюты
     * @return {@link CurrencyResponse} содержащий стоимость и время получения стоимости
     */
    public CurrencyResponse get(String currencyName) {
        return currencyResponseMap.get(currencyName);
    }
}
