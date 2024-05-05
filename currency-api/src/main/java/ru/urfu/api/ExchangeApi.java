package ru.urfu.api;

import ru.urfu.model.CurrencyResponse;
import ru.urfu.model.ApiDescription;
import ru.urfu.utils.JsonParser;
import ru.urfu.utils.CurrencyCache;
import ru.urfu.utils.RequestSender;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Класс позволяющий получать стоимость валют используя Exchange API
 */
public class ExchangeApi implements CurrencyApi {

    private static final String NAME = "Exchange API";
    private static final ApiDescription DESCRIPTION = new ApiDescription(NAME, "https://github.com/fawazahmed0/exchange-api", "1 день");
    private static final String URL = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/usd.json";

    private final RequestSender requestSender;
    private final JsonParser jsonParser;

    private final Map<String, String> currencyPathMap = Collections.unmodifiableMap(new LinkedHashMap<>() {{
        put("Рубль", "usd.rub");
        put("Евро", "usd.eur");
        put("Йен", "usd.jpy");
        put("Юань", "usd.cny");
        put("Франк", "usd.chf");
        put("Рупий", "usd.inr");
    }});

    private final Duration updateDuration = Duration.ofDays(1);
    private final CurrencyCache cache = new CurrencyCache(updateDuration);

    public ExchangeApi(RequestSender requestSender, JsonParser jsonParser) {
        this.requestSender = requestSender;
        this.jsonParser = jsonParser;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION.toString();
    }

    @Override
    public Set<String> getCurrencies() {
        return currencyPathMap.keySet();
    }

    @Override
    public CurrencyResponse getPrice(String currency) {

        if (cache.notContains(currency)) {
            String response = requestSender.sendGetRequest(URL);

            currencyPathMap.forEach((curr, path) -> {
                double price = 1 / Double.parseDouble(jsonParser.parse(response, path));
                cache.save(curr, price);
            });
        }

        return cache.get(currency);
    }
}
