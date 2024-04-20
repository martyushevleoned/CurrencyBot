package ru.urfu;

import ru.urfu.api.*;
import ru.urfu.model.CurrencyRequest;
import ru.urfu.model.CurrencyResponse;
import ru.urfu.utils.JsonParser;
import ru.urfu.utils.RequestSender;

import java.util.*;

/**
 * Единая точка для обращения к любому из подключённых API
 */
public class ApiManager {

    private final RequestSender requestSender = new RequestSender();
    private final JsonParser jsonParser = new JsonParser();

    private final Map<String, CurrencyApi> currencyApiMap = new LinkedHashMap<>() {{
        put("CoinCap API", new CoinCapApi(requestSender, jsonParser));
        put("ExchangeRate API", new ExchangeRateApi(requestSender, jsonParser));
        put("Exchange API", new ExchangeApi(requestSender, jsonParser));
        put("Cbr API", new CbrApi(requestSender, jsonParser));
    }};

    private final Set<CurrencyRequest> currencyRequestList = new LinkedHashSet<>() {{
        currencyApiMap.forEach((apiName, api) ->
                api.getCurrencies().forEach(currency ->
                        add(new CurrencyRequest(apiName, currency))
                )
        );
    }};

    /**
     * Возвращает названия всех доступных API
     */
    public Set<String> getAllApi() {
        return currencyApiMap.keySet();
    }

    /**
     * Возвращает названия все возможные корректные запросы
     */
    public Set<CurrencyRequest> getPossibleRequests() {
        return currencyRequestList;
    }

    /**
     * Возвращает стоимость валюты на основе названия API и валюты
     */
    public CurrencyResponse getPrice(CurrencyRequest currencyRequest) {
        assert currencyApiMap.containsKey(currencyRequest.getApi());
        CurrencyApi currencyApi = currencyApiMap.get(currencyRequest.getApi());

        assert currencyApi.getCurrencies().contains(currencyRequest.getCurrency());
        return currencyApi.getPrice(currencyRequest.getCurrency());
    }

    /**
     * Возвращает описание API по названию API
     */
    public String getDescription(String apiName){
        assert currencyApiMap.containsKey(apiName);
        return currencyApiMap.get(apiName).getDescription();
    }
}
