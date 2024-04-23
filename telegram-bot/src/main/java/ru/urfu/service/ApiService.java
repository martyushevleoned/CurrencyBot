package ru.urfu.service;

import org.springframework.stereotype.Service;
import ru.urfu.ApiManager;
import ru.urfu.api.CbrApi;
import ru.urfu.api.CoinCapApi;
import ru.urfu.api.ExchangeApi;
import ru.urfu.api.ExchangeRateApi;
import ru.urfu.model.CurrencyRequest;
import ru.urfu.model.CurrencyResponse;
import ru.urfu.utils.JsonParser;
import ru.urfu.utils.RequestSender;

import java.util.List;
import java.util.Set;

/**
 * Класс для взаимодействия с модулем currency-api
 */
@Service
public class ApiService {

    private final RequestSender requestSender = new RequestSender();
    private final JsonParser jsonParser = new JsonParser();
    private final ApiManager apiManager = new ApiManager(List.of(
            new CoinCapApi(requestSender, jsonParser),
            new ExchangeApi(requestSender, jsonParser),
            new ExchangeRateApi(requestSender, jsonParser),
            new CbrApi(requestSender, jsonParser)
    ));

    /**
     * Получить все доступные запросы к API
     */
    public Set<CurrencyRequest> getPossibleRequests() {
        return apiManager.getPossibleRequests();
    }

    /**
     * Получить стоимость валюты из модуля currency-api
     *
     * @param currencyRequest запрос из модуля currency-api
     */
    public CurrencyResponse getPrice(CurrencyRequest currencyRequest) {
        return apiManager.getPrice(currencyRequest);
    }

    /**
     * Получить отсортированный список всех используемых API
     */
    public Set<String> getAllApiNames() {
        return apiManager.getAllApiNames();
    }

    /**
     * Получить описание API по названию
     *
     * @param apiName название API
     */
    public String getDescription(String apiName) {
        return apiManager.getDescription(apiName);
    }
}
