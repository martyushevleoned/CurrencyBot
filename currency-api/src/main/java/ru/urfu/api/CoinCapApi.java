package ru.urfu.api;

import ru.urfu.model.CurrencyResponse;
import ru.urfu.model.ApiDescription;
import ru.urfu.utils.CurrencyCache;
import ru.urfu.utils.JsonParser;
import ru.urfu.utils.RequestSender;

import java.time.Duration;
import java.util.Map;
import java.util.Set;

/**
 * Класс позволяющий получать стоимость валют используя оффициальный API CoinCap
 */
public class CoinCapApi implements CurrencyApi {

    private static final String NAME = "CoinCap API";
    private static final ApiDescription DESCRIPTION = new ApiDescription(NAME, "https://coincap.io/", "1 минута");

    private final RequestSender requestSender;
    private final JsonParser jsonParser;

    private final Map<String, String> currencyUrlMap = Map.of(
            "Bitcoin", "https://api.coincap.io/v2/rates/bitcoin",
            "Ethereum", "https://api.coincap.io/v2/rates/ethereum",
            "Litecoin", "https://api.coincap.io/v2/rates/litecoin",
            "Dogecoin", "https://api.coincap.io/v2/rates/dogecoin",
            "Tether", "https://api.coincap.io/v2/rates/tether"
    );

    private final Duration updateDuration = Duration.ofMinutes(1);
    private final CurrencyCache cache = new CurrencyCache(updateDuration);

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
        return DESCRIPTION.toString();
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

            double price = Double.parseDouble(jsonParser.parse(response, "data.rateUsd"));
            cache.save(currency, price);
        }

        return cache.get(currency);
    }
}
