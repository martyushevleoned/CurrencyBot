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
 * Класс позволяющий получать стоимость валют используя официальный API центробанка
 */
@Component
public class CbrApi implements CurrencyApi {

    private final RequestSender requestSender;
    private final JsonParser jsonParser;

    private final String NAME = "Cbr API";
    private final String URL = "https://www.cbr-xml-daily.ru/latest.js";
    private final String DESCRIPTION = new ApiDescription(NAME, "https://www.cbr-xml-daily.ru/", "1 день").getDescription();
    private final CurrencyCache cache = new CurrencyCache(Duration.ofDays(1));

    private final Map<String, String> currencyPathMap = Map.of(
            "Евро", "$.rates.EUR",
            "Йен", "$.rates.JPY",
            "Юань", "$.rates.CNY",
            "Рупий", "$.rates.INR"
    );

    @Autowired
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

            double usdPrice = jsonParser.parseDouble(response, "rates.USD");
            currencyPathMap.forEach((curr, path) -> {
                double price = usdPrice / jsonParser.parseDouble(response, path);
                cache.save(curr, price);
            });
        }

        return cache.get(currency);
    }
}
