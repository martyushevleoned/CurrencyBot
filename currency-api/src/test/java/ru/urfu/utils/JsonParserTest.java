package ru.urfu.utils;

import org.junit.Assert;
import org.junit.Test;

public class JsonParserTest {

    private final JsonParser jsonParser = new JsonParser();

    @Test
    public void parse() {
        String json = "{" +
                "\"data\":" +
                "{\"id\":\"litecoin\"," +
                "\"symbol\":\"LTC\"," +
                "\"currencySymbol\":null," +
                "\"type\":\"crypto\"," +
                "\"rateUsd\":\"82.5696619464591515\"" +
                "}," +
                "\"timestamp\":1713616056895" +
                "}";

        String id = jsonParser.parse(json, "data.id");
        Assert.assertEquals("litecoin", id);

        String symbol = jsonParser.parse(json, "data.symbol");
        Assert.assertEquals("LTC", symbol);

        String type = jsonParser.parse(json, "data.type");
        Assert.assertEquals("crypto", type);

        String rateUsd = jsonParser.parse(json, "data.rateUsd");
        Assert.assertEquals("82.5696619464591515", rateUsd);

        String timestamp = jsonParser.parse(json, "timestamp");
        Assert.assertEquals("1713616056895", timestamp);
    }
}