package ru.urfu.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.urfu.model.ApiDescription;
import ru.urfu.model.CurrencyResponse;
import ru.urfu.utils.CurrencyCache;
import ru.urfu.utils.JsonParser;
import ru.urfu.utils.RequestSender;

import java.time.Duration;
import java.util.Map;
import java.util.Set;

/**
 * Класс позволяющий получать стоимость валют используя Exchange API
 */
@Component
public class ExchangeApi implements CurrencyApi {

    private final RequestSender requestSender;
    private final JsonParser jsonParser;

    private final String NAME = "Exchange API";
    private final String URL = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/usd.json";
    private final String DESCRIPTION = new ApiDescription(NAME, "https://github.com/fawazahmed0/exchange-api", "1 день").getDescription();
    private final CurrencyCache cache = new CurrencyCache(Duration.ofDays(1));

    private final Map<String, String> currencyPathMap = Map.of(
            "Рубль", "$.usd.rub",
            "Евро", "$.usd.eur",
            "Йен", "$.usd.jpy",
            "Юань", "$.usd.cny",
            "Франк", "$.usd.chf",
            "Рупий", "$.usd.inr"
    );

    @Autowired
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
        return DESCRIPTION;
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
