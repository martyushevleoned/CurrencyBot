package ru.urfu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.model.Request;

import java.util.*;

/**
 * Класс для хранения того какие валюты отслеживает пользователь
 */
@Service
public class TrackService {

    @Autowired
    private ApiService apiService;

    private final Map<Long, Set<Request>> trackedCurrencies = new HashMap<>();

    /**
     * Добавляет валюту в список отслеживаемых / удаляет из отслеживаемых уже добавленные валюты
     */
    public void addToTrack(long chatId, Request request) {
        if (!trackedCurrencies.containsKey(chatId)) {
            trackedCurrencies.put(chatId, new LinkedHashSet<>(Set.of(request)));
            return;
        }

        if (trackedCurrencies.get(chatId).contains(request)) {
            trackedCurrencies.get(chatId).remove(request);
        } else {
            trackedCurrencies.get(chatId).add(request);
        }
    }

    /**
     * Возвращает список всех валют ослеживаемых пользователем
     */
    public List<Request> getTrackedRequests(long chatId){
        return trackedCurrencies.getOrDefault(chatId, Set.of()).stream().toList();
    }

    /**
     * Проверяет отслеживается ли валюта пользователем
     */
    public boolean isTracked(long chatId, Request request) {
        if (!trackedCurrencies.containsKey(chatId))
            return false;
        else {
            return trackedCurrencies.get(chatId).contains(request);
        }
    }
}
