package ru.urfu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.api.CurrencyApi;
import ru.urfu.exceptions.ApiNotFoundException;
import ru.urfu.exceptions.ApiNotSupportedCurrencyException;
import ru.urfu.model.CurrencyRequest;
import ru.urfu.model.CurrencyResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Единая точка для обращения к любому из подключённых API
 */
@Service
public class ApiService {

    private final Map<String, CurrencyApi> currencyApiMap;
    private final Set<CurrencyRequest> currencyRequests;

    @Autowired
    public ApiService(List<CurrencyApi> currencyApiList) {

        currencyApiMap = currencyApiList.stream().collect(Collectors.toUnmodifiableMap(CurrencyApi::getName, Function.identity()));

        currencyRequests = currencyApiList.stream()
                .flatMap(currencyApi -> currencyApi.getCurrencies().stream()
                        .map(currency -> new CurrencyRequest(currencyApi.getName(), currency)))
                .collect(Collectors.toUnmodifiableSet());
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
     * @return {@link CurrencyResponse} содержащий стоимость валюты и время получения стоимости
     * @throws ApiNotFoundException если API не существует
     * @throws ApiNotSupportedCurrencyException если API не поддерживает валюту
     */
    public CurrencyResponse getPrice(CurrencyRequest currencyRequest) {

        if (!currencyApiMap.containsKey(currencyRequest.api()))
            throw new ApiNotFoundException("Указанный API не существует");

        CurrencyApi currencyApi = currencyApiMap.get(currencyRequest.api());
        String currency = currencyRequest.currency();

        if (!currencyApi.getCurrencies().contains(currency))
            throw new ApiNotSupportedCurrencyException("API не поддерживает данную валюту");

        return currencyApi.getPrice(currency);
    }

    /**
     * Получить описание API по названию
     *
     * @param apiName название API
     * @return текстовое описание API
     * @throws ApiNotSupportedCurrencyException если API не поддерживает валюту
     */
    public String getDescription(String apiName) {

        if (!currencyApiMap.containsKey(apiName))
            throw new ApiNotFoundException("Указанный API не существует");

        return currencyApiMap.get(apiName).getDescription();
    }
}
