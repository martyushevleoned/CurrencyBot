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
 * Класс позволяющий получать стоимость валют используя официальный API CoinCap
 */
@Component
public class CoinCapApi implements CurrencyApi {

    private final RequestSender requestSender;
    private final JsonParser jsonParser;

    private final String NAME = "CoinCap API";
    private final String DESCRIPTION = new ApiDescription(NAME, "https://coincap.io/", "1 минута").getDescription();
    private final CurrencyCache cache = new CurrencyCache(Duration.ofMinutes(1));

    private final Map<String, String> currencyUrlMap = Map.of(
            "Bitcoin", "https://api.coincap.io/v2/rates/bitcoin",
            "Ethereum", "https://api.coincap.io/v2/rates/ethereum",
            "Litecoin", "https://api.coincap.io/v2/rates/litecoin",
            "Dogecoin", "https://api.coincap.io/v2/rates/dogecoin",
            "Tether", "https://api.coincap.io/v2/rates/tether"
    );

    @Autowired
    public CoinCapApi(RequestSender requestSender, JsonParser jsonParser) {
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
        return currencyUrlMap.keySet();
    }

    @Override
    public CurrencyResponse getPrice(String currency) {

        if (cache.notContains(currency)) {
            String url = currencyUrlMap.get(currency);
            String response = requestSender.sendGetRequest(url);

            double price = jsonParser.parseDouble(response, "$.data.rateUsd");
            cache.save(currency, price);
        }

        return cache.get(currency);
    }
}
