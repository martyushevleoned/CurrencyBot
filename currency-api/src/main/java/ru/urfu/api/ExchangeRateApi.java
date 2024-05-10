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
 * Класс позволяющий получать стоимость валют используя ExchangeRate API
 */
@Component
public class ExchangeRateApi implements CurrencyApi {

    private final RequestSender requestSender;

    private final JsonParser jsonParser;
    private final String NAME = "ExRate API";
    private final String URL = "https://open.er-api.com/v6/latest/USD";
    private final String DESCRIPTION = new ApiDescription(NAME, "https://www.exchangerate-api.com/", "1 день").getDescription();
    private final CurrencyCache cache = new CurrencyCache(Duration.ofDays(1));

    private final Map<String, String> currencyPathMap = Map.of(
            "Рубль", "$.rates.RUB",
            "Евро", "$.rates.EUR",
            "Йен", "$.rates.JPY",
            "Юань", "$.rates.CNY",
            "Франк", "$.rates.CHF",
            "Рупий", "$.rates.INR",
            "Песо", "$.rates.ARS"
    );

    @Autowired
    public ExchangeRateApi(RequestSender requestSender, JsonParser jsonParser) {
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
