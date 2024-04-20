package ru.urfu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.ApiManager;
import ru.urfu.model.CurrencyRequest;
import ru.urfu.model.CurrencyResponse;
import ru.urfu.utils.DataConverter;

import java.util.List;
import java.util.Set;

/**
 * Класс для взаимодействия с модулем currency-api
 */
@Service
public class ApiService {

    private final ApiManager apiManager = new ApiManager();

    @Autowired
    private DataConverter dataConverter;

    /**
     * Возвращает все доступные запросы к API
     */
    public Set<CurrencyRequest> getPossibleRequests() {
        return apiManager.getPossibleRequests();
    }

    /**
     * Возвращает стоимость валюты из модуля currency-api
     */
    public CurrencyResponse getPrice(CurrencyRequest currencyRequest) {
        return apiManager.getPrice(currencyRequest);
    }

    /**
     * Возвращает отсортированный список всех используемых API
     */
    public List<String> getAllApiNames(){
        return apiManager.getAllApi().stream().sorted().toList();
    }

    /**
     * Возвращает описание API по названию
     */
    public String getDescription(String apiName) {
        return apiManager.getDescription(apiName);
    }
}
