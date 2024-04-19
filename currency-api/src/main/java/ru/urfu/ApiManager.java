package ru.urfu;

import ru.urfu.api.CoinCapCryptoApi;
import ru.urfu.api.CoinCapFiatApi;
import ru.urfu.api.CurrencyApi;
import ru.urfu.model.Request;
import ru.urfu.model.Response;
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
        CurrencyApi coinCapCryptoApi = new CoinCapCryptoApi(requestSender, jsonParser);
        put(coinCapCryptoApi.getName(), coinCapCryptoApi);
//        CurrencyApi coinCapFiatApi = new CoinCapFiatApi(requestSender, jsonParser);
//        put(coinCapFiatApi.getName(), coinCapFiatApi);
    }};

    private final Set<Request> requestList = new LinkedHashSet<>() {{
        currencyApiMap.forEach((apiName, api) ->
                api.getCurrencies().forEach(currency ->
                        add(new Request(apiName, currency))
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
    public Set<Request> getPossibleRequests() {
        return requestList;
    }

    /**
     * Возвращает стоимость валюты на основе названия API и валюты
     */
    public Response getPrice(Request request) {
        assert currencyApiMap.containsKey(request.getApi());
        CurrencyApi currencyApi = currencyApiMap.get(request.getApi());

        assert currencyApi.getCurrencies().contains(request.getCurrency());
        return currencyApi.getPrice(request.getCurrency());
    }
}
