package ru.urfu.api;

import ru.urfu.model.CurrencyResponse;
import ru.urfu.utils.JsonParser;
import ru.urfu.utils.RequestEconomizer;
import ru.urfu.utils.RequestSender;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class CbrApi implements CurrencyApi {

    private final RequestSender requestSender;
    private final JsonParser jsonParser;

    private final Map<String, String> currencyPathMap = new LinkedHashMap<>() {{
        put("Евро", "rates.EUR");
        put("Йен", "rates.JPY");
        put("Юань", "rates.CNY");
        put("Рупий", "rates.INR");
    }};

    private final Duration updateDuration = Duration.ofDays(1);
    private final RequestEconomizer economizer = new RequestEconomizer(updateDuration);

    public CbrApi(RequestSender requestSender, JsonParser jsonParser) {
        this.requestSender = requestSender;
        this.jsonParser = jsonParser;
    }

    @Override
    public String getDescription() {
        return "Оффициальный сайт: https://www.cbr-xml-daily.ru/." +
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
            String url = "https://www.cbr-xml-daily.ru/latest.js";
            String response = requestSender.send(url);

            double usdPrice = Double.parseDouble(jsonParser.parse(response, "rates.USD"));
            currencyPathMap.forEach((curr, path) -> {
                double price = usdPrice / Double.parseDouble(jsonParser.parse(response, path));
                economizer.save(curr, price);
            });
        }

        return economizer.getFromCache(currency);
    }
}
