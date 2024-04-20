package ru.urfu.api;

import ru.urfu.model.CurrencyResponse;
import ru.urfu.utils.JsonParser;
import ru.urfu.utils.RequestEconomizer;
import ru.urfu.utils.RequestSender;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ExchangeApi implements CurrencyApi {

    private final RequestSender requestSender;
    private final JsonParser jsonParser;

    private final Map<String, String> currencyPathMap = new LinkedHashMap<>() {{
        put("Рубль", "usd.rub");
        put("Евро", "usd.eur");
        put("Йен", "usd.jpy");
        put("Юань", "usd.cny");
        put("Франк", "usd.chf");
        put("Рупий", "usd.inr");
    }};

    private final Duration updateDuration = Duration.ofDays(1);
    private final RequestEconomizer economizer = new RequestEconomizer(updateDuration);

    public ExchangeApi(RequestSender requestSender, JsonParser jsonParser) {
        this.requestSender = requestSender;
        this.jsonParser = jsonParser;
    }


    @Override
    public String getDescription() {
        return "Оффициальный сайт: https://github.com/fawazahmed0/exchange-api." +
                "\nПериод обновления курса: 1 день.";
    }

    @Override
    public Set<String> getCurrencies() {
        return currencyPathMap.keySet();
    }

    @Override
    public CurrencyResponse getPrice(String currency) {
        assert currencyPathMap.containsKey(currency);

        if (economizer.notContains(currency)) {
            String url = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/usd.json";
            String response = requestSender.send(url);

            currencyPathMap.forEach((curr, path) -> {
                double price = 1 / Double.parseDouble(jsonParser.parse(response, path));
                economizer.save(curr, price);
            });
        }

        return economizer.getFromCache(currency);
    }
}
