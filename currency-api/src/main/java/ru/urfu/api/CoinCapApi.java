package ru.urfu.api;

import ru.urfu.model.CurrencyResponse;
import ru.urfu.utils.RequestEconomizer;
import ru.urfu.utils.JsonParser;
import ru.urfu.utils.RequestSender;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CoinCapApi implements CurrencyApi {

    private final RequestSender requestSender;
    private final JsonParser jsonParser;

    private final Map<String, String> currencyUrlMap = new LinkedHashMap<>() {{
        put("Bitcoin", "https://api.coincap.io/v2/rates/bitcoin");
        put("Ethereum", "https://api.coincap.io/v2/rates/ethereum");
        put("Litecoin", "https://api.coincap.io/v2/rates/litecoin");
        put("Dogecoin", "https://api.coincap.io/v2/rates/dogecoin");
        put("Tether", "https://api.coincap.io/v2/rates/tether");
    }};

    private final Duration updateDuration = Duration.ofMinutes(1);
    private final RequestEconomizer economizer = new RequestEconomizer(updateDuration);

    public CoinCapApi(RequestSender requestSender, JsonParser jsonParser) {
        this.requestSender = requestSender;
        this.jsonParser = jsonParser;
    }

    @Override
    public String getDescription() {
        return "Оффициальный сайт: https://coincap.io/." +
                "\nПериод обновления курса: 1 минута.";
    }

    @Override
    public Set<String> getCurrencies() {
        return currencyUrlMap.keySet();
    }

    @Override
    public CurrencyResponse getPrice(String currency) {
        assert currencyUrlMap.containsKey(currency);
        String url = currencyUrlMap.get(currency);

        if (economizer.notContains(currency)) {
            String response = requestSender.send(url);

            double price = Double.parseDouble(jsonParser.parse(response, "data.rateUsd"));
            economizer.save(currency, price);
        }

        return economizer.getFromCache(currency);
    }
}
