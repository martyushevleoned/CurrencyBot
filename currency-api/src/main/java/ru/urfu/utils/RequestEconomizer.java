package ru.urfu.utils;

import ru.urfu.model.Response;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Простая реализация кэша
 */
public class RequestEconomizer {

    private final long updateTime;
    private final Map<String, Response> history = new HashMap<>();

    /**
     * updateTime - минимальный период обновления значения
     */
    public RequestEconomizer(Duration updateTime) {
        this.updateTime = updateTime.toMillis();
    }

    /**
     * проверяет наличие значения стоимости сохранённого менее чем указанное время назад.
     */
    public boolean isEconomizable(String request) {
        if (!history.containsKey(request))
            return false;
        return new Date().getTime() - history.get(request).getDatetime().getTime() < updateTime;
    }

    /**
     * сохраняет значение в кэш
     */
    public void save(String request, double response) {
        history.put(request, new Response(response, new Date()));
    }

    /**
     * получает значение из кэша
     */
    public Response getFromCache(String request) {
        return history.get(request);
    }
}
