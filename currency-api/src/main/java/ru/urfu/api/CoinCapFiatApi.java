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
        put("Belarusian ruble", "data.63.rateUsd");
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
