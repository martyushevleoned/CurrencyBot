package ru.urfu.service;

import org.springframework.stereotype.Service;
import ru.urfu.model.CurrencyRequest;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Класс для хранения запросов отслеживаемых пользователем
 */
@Service
public class TrackService {

    private final Map<Long, Set<CurrencyRequest>> trackedCurrencies = new HashMap<>();

    /**
     * Добавить запрос в список отслеживаемых / удаляет из отслеживаемых уже добавленные валюты
     *
     * @param chatId          id чата
     * @param currencyRequest отслеживаемый пользователем запрос к ApiService
     */
    public void addToTrack(long chatId, CurrencyRequest currencyRequest) {
        if (!trackedCurrencies.containsKey(chatId)) {
            trackedCurrencies.put(chatId, new LinkedHashSet<>(Set.of(currencyRequest)));
            return;
        }

        trackedCurrencies.get(chatId).add(currencyRequest);
    }

    public void removeFromTrack(long chatId, CurrencyRequest currencyRequest) {
        if (isTracked(chatId, currencyRequest))
            trackedCurrencies.get(chatId).remove(currencyRequest);
    }

    /**
     * Получить список всех запросов, отслеживаемых пользователем
     *
     * @param chatId id чата
     */
    public Set<CurrencyRequest> getTrackedRequests(long chatId) {
        return trackedCurrencies.getOrDefault(chatId, Set.of());
    }

    /**
     * Отслеживается ли запрос пользователем
     *
     * @param chatId          id чата
     * @param currencyRequest запрос
     */
    public boolean isTracked(long chatId, CurrencyRequest currencyRequest) {
        if (trackedCurrencies.containsKey(chatId))
            return trackedCurrencies.get(chatId).contains(currencyRequest);
        return false;
    }
}
