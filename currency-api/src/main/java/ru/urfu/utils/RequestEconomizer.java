package ru.urfu.utils;

import ru.urfu.model.CurrencyResponse;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Простая реализация кэша
 */
public class RequestEconomizer {

    private final long updateTime;
    private final Map<String, CurrencyResponse> history = new HashMap<>();

    /**
     * updateTime - минимальный период обновления значения
     */
    public RequestEconomizer(Duration updateTime) {
        this.updateTime = updateTime.toMillis();
    }

    /**
     * проверяет наличие значения стоимости сохранённого менее чем указанное время назад.
     */
    public boolean contains(String currency) {
        if (!history.containsKey(currency))
            return false;
        return new Date().getTime() - history.get(currency).getDatetime().getTime() < updateTime;
    }

    /**
     * проверяет отсутствие значения стоимости сохранённого менее чем указанное время назад.
     */
    public boolean notContains(String key){
        return !contains(key);
    }

    /**
     * сохраняет значение в кэш
     */
    public void save(String currency, double price) {
        history.put(currency, new CurrencyResponse(price, new Date()));
    }

    /**
     * получает значение из кэша
     */
    public CurrencyResponse getFromCache(String request) {
        return history.get(request);
    }
}
