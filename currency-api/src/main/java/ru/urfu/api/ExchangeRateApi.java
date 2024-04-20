package ru.urfu.api;

import ru.urfu.model.CurrencyResponse;
import ru.urfu.utils.JsonParser;
import ru.urfu.utils.RequestEconomizer;
import ru.urfu.utils.RequestSender;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ExchangeRateApi implements CurrencyApi {

    private final RequestSender requestSender;
    private final JsonParser jsonParser;

    private final Map<String, String> currencyPathMap = new LinkedHashMap<>() {{
        put("Рубль", "rates.RUB");
        put("Евро", "rates.EUR");
        put("Йен", "rates.JPY");
        put("Юань", "rates.CNY");
        put("Франк", "rates.CHF");
        put("Рупий", "rates.INR");
        put("Песо", "rates.ARS");
    }};

    private final Duration updateDuration = Duration.ofDays(1);
    private final RequestEconomizer economizer = new RequestEconomizer(updateDuration);

    public ExchangeRateApi(RequestSender requestSender, JsonParser jsonParser) {
        this.requestSender = requestSender;
        this.jsonParser = jsonParser;
    }


    @Override
    public String getDescription() {
        return "Оффициальный сайт: https://www.exchangerate-api.com/." +
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
            String url = "https://open.er-api.com/v6/latest/USD";
            String response = requestSender.send(url);

            currencyPathMap.forEach((curr, path) -> {
                double price = 1 / Double.parseDouble(jsonParser.parse(response, path));
                economizer.save(curr, price);
            });
        }

        return economizer.getFromCache(currency);
    }
}
