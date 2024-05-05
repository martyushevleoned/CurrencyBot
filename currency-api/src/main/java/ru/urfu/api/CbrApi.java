package ru.urfu.api;

import ru.urfu.model.CurrencyResponse;
import ru.urfu.model.ApiDescription;
import ru.urfu.utils.JsonParser;
import ru.urfu.utils.CurrencyCache;
import ru.urfu.utils.RequestSender;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Класс позволяющий получать стоимость валют используя оффициальный API центробанка
 */
public class CbrApi implements CurrencyApi {

    private static final String NAME = "Cbr API";
    private static final ApiDescription DESCRIPTION = new ApiDescription(NAME, "https://www.cbr-xml-daily.ru/", "1 день");
    private static final String URL = "https://www.cbr-xml-daily.ru/latest.js";

    private final RequestSender requestSender;
    private final JsonParser jsonParser;

    private final Map<String, String> currencyPathMap = Collections.unmodifiableMap(new LinkedHashMap<>() {{
        put("Евро", "rates.EUR");
        put("Йен", "rates.JPY");
        put("Юань", "rates.CNY");
        put("Рупий", "rates.INR");
    }});

    private final Duration updateDuration = Duration.ofDays(1);
    private final CurrencyCache cache = new CurrencyCache(updateDuration);

    public CbrApi(RequestSender requestSender, JsonParser jsonParser) {
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
        return currencyPathMap.keySet();
    }

    @Override
    public CurrencyResponse getPrice(String currency) {

        if (cache.notContains(currency)) {
            String response = requestSender.sendGetRequest(URL);

            double usdPrice = Double.parseDouble(jsonParser.parse(response, "rates.USD"));
            currencyPathMap.forEach((curr, path) -> {
                double price = usdPrice / Double.parseDouble(jsonParser.parse(response, path));
                cache.save(curr, price);
            });
        }

        return cache.get(currency);
    }
}
