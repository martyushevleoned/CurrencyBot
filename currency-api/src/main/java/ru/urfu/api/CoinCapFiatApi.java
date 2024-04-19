package ru.urfu.api;

import ru.urfu.model.Response;
import ru.urfu.utils.RequestEconomizer;
import ru.urfu.utils.JsonParser;
import ru.urfu.utils.RequestSender;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CoinCapFiatApi implements CurrencyApi {

    private final RequestSender requestSender;
    private final JsonParser jsonParser;

    private final Map<String, String> currencyPathMap = new LinkedHashMap<>() {{
        put("Belarusian ruble", "data.25.rateUsd");
        put("Egyptian pound", "data.36.rateUsd");
        put("Turkish lira", "data.37.rateUsd");
        put("Kazakhstani tenge", "data.38.rateUsd");
        put("Russian ruble", "data.96.rateUsd");
        put("Euro", "data.131.rateUsd");
        put("Japanese yen", "data.132.rateUsd");
        put("Indian rupee", "data.141.rateUsd");
        put("Mexican peso", "data.146.rateUsd");
    }};

    private final Duration updateDuration = Duration.ofDays(1);
    private final RequestEconomizer economizer = new RequestEconomizer(updateDuration);

    public CoinCapFiatApi(RequestSender requestSender, JsonParser jsonParser) {
        this.requestSender = requestSender;
        this.jsonParser = jsonParser;
    }

    @Override
    public String getName() {
        return "CoinCap (Fiat)";
    }

    @Override
    public Set<String> getCurrencies() {
        return currencyPathMap.keySet();
    }

    @Override
    public Response getPrice(String currency) {
        assert currencyPathMap.containsKey(currency);
        String url = "https://api.coincap.io/v2/rates";

        if (!economizer.isEconomizable(currency)) {
            String response = requestSender.send(url);

            currencyPathMap.forEach((name, path) -> {
                double value = Double.parseDouble(jsonParser.parse(response, path));
                economizer.save(name, value);
            });
        }

        return economizer.getFromCache(currency);
    }
}
