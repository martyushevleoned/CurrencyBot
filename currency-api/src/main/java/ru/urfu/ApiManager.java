package ru.urfu;

import ru.urfu.api.*;
import ru.urfu.exceptions.ApiNotFoundException;
import ru.urfu.exceptions.ApiNotSupportedCurrencyException;
import ru.urfu.model.CurrencyRequest;
import ru.urfu.model.CurrencyResponse;

import java.util.*;

/**
 * Единая точка для обращения к любому из подключённых API
 */
public class ApiManager {

    private final Map<String, CurrencyApi> currencyApiMap;
    private final Set<CurrencyRequest> currencyRequests;

    public ApiManager(List<CurrencyApi> currencyApiList) {
        currencyApiMap = Collections.unmodifiableMap(new HashMap<>() {{
            currencyApiList.forEach(currencyApi ->
                    put(currencyApi.getName(), currencyApi)
            );
        }});

        currencyRequests = Collections.unmodifiableSet(new HashSet<>() {{
            currencyApiList.forEach(currencyApi ->
                currencyApi.getCurrencies().forEach(currency ->
                        add(new CurrencyRequest(currencyApi.getName(), currency))
                )
            );
        }});
    }

    /**
     * Получить названия всех доступных API
     */
    public Set<String> getAllApiNames() {
        return currencyApiMap.keySet();
    }

    /**
     * Получить все поддерживаемые запросы
     */
    public Set<CurrencyRequest> getPossibleRequests() {
        return currencyRequests;
    }

    /**
     * Получить стоимость валюты на основе названия API и валюты
     *
     * @param currencyRequest запрос с информацией о запрашиваемой валюте и названием API
     * @return {@link CurrencyResponse} содержаший стоимость валюты и время получения стоимости
     */
    public CurrencyResponse getPrice(CurrencyRequest currencyRequest) {

        if (!currencyApiMap.containsKey(currencyRequest.getApi()))
            throw new ApiNotFoundException("Указанный API не существует");

        CurrencyApi currencyApi = currencyApiMap.get(currencyRequest.getApi());

        if (!currencyApi.getCurrencies().contains(currencyRequest.getCurrency()))
            throw new ApiNotSupportedCurrencyException("API не поддерживает данную валюту");

        return currencyApi.getPrice(currencyRequest.getCurrency());
    }

    /**
     * Получить описание API по названию
     *
     * @param apiName название API
     * @return текстовое описание API
     */
    public String getDescription(String apiName) {

        if (!currencyApiMap.containsKey(apiName))
            throw new ApiNotFoundException("Указанный API не существует");

        return currencyApiMap.get(apiName).getDescription();
    }
}
