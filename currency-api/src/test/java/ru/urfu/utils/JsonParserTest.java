package ru.urfu.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Тестирование класса {@link JsonParser}
 */
public class JsonParserTest {

    private final JsonParser jsonParser = new JsonParser();

    /**
     * Тестирование корректности извлечения значения из json по ключу
     * <ul>
     *     <li>Создаёт json в виде строки</li>
     *     <li>Получает и проверяет значения полей:
     *     <ol>
     *        <li>data.id</li>
     *        <li>data.symbol</li>
     *        <li>data.type</li>
     *        <li>data.currencySymbol</li>
     *        <li>data.rateUsd</li>
     *        <li>timestamp</li>
     *     </ol>
     *     </li>
     * </ul>
     */
    @Test
    public void parseJsonObjectTest() {
        String json = """
                {
                  "data": {
                    "id": "litecoin",
                    "symbol": "LTC",
                    "currencySymbol": null,
                    "type": "crypto",
                    "rateUsd": "82.5696619464591515"
                  },
                  "timestamp": 1713616056895
                }
                """;

        String id = jsonParser.parse(json, "data.id");
        Assert.assertEquals("litecoin", id);

        String symbol = jsonParser.parse(json, "data.symbol");
        Assert.assertEquals("LTC", symbol);

        String type = jsonParser.parse(json, "data.type");
        Assert.assertEquals("crypto", type);

        String currencySymbol = jsonParser.parse(json, "data.currencySymbol");
        Assert.assertEquals("null", currencySymbol);

        String rateUsd = jsonParser.parse(json, "data.rateUsd");
        Assert.assertEquals("82.5696619464591515", rateUsd);

        String timestamp = jsonParser.parse(json, "timestamp");
        Assert.assertEquals("1713616056895", timestamp);
    }

    /**
     * Тестирование корректности извлечения значения из json по индексу массива
     * <ul>
     *     <li>Создаёт json в виде строки</li>
     *     <li>Получает и проверяет значения полей:
     *     <ol>
     *        <li>phoneNumbers.[0]</li>
     *        <li>phoneNumbers.[1]</li>
     *     </ol>
     *     </li>
     *     <li>Создаёт новый json в виде строки</li>
     *     <li>Получает и проверяет значения полей:
     *     <ol>
     *        <li>menu.popup.menuitem.[0].value</li>
     *        <li>menu.popup.menuitem.[0].onclick</li>
     *        <li>menu.popup.menuitem.[1].value</li>
     *        <li>menu.popup.menuitem.[1].onclick</li>
     *        <li>menu.popup.menuitem.[2].value</li>
     *        <li>menu.popup.menuitem.[2].onclick</li>
     *     </ol>
     *     </li>
     * </ul>
     */
    @Test
    public void parseJsonArray() {

        String json = """
                {
                  "firstName": "Иван",
                  "lastName": "Иванов",
                  "address": {
                    "streetAddress": "Московское ш., 101, кв.101",
                    "city": "Ленинград",
                    "postalCode": 101101
                  },
                  "phoneNumbers": [
                    "812 123-1234",
                    "916 123-4567"
                  ]
                }
                """;


        String firstPhoneNumber = jsonParser.parse(json, "phoneNumbers.0");
        Assert.assertEquals("812 123-1234", firstPhoneNumber);

        String secondPhoneNumber = jsonParser.parse(json, "phoneNumbers.1");
        Assert.assertEquals("916 123-4567", secondPhoneNumber);

        json = """
                {
                  "menu": {
                    "id": "file",
                    "value": "File",
                    "popup": {
                      "menuitem": [
                        {
                          "value": "New",
                          "onclick": "CreateNewDoc()"
                        },
                        {
                          "value": "Open",
                          "onclick": "OpenDoc()"
                        },
                        {
                          "value": "Close",
                          "onclick": "CloseDoc()"
                        }
                      ]
                    }
                  }
                }
                """;

        String firstMenuItemValue = jsonParser.parse(json, "menu.popup.menuitem.0.value");
        Assert.assertEquals("New", firstMenuItemValue);

        String firstMenuItemOnclick = jsonParser.parse(json, "menu.popup.menuitem.0.onclick");
        Assert.assertEquals("CreateNewDoc()", firstMenuItemOnclick);

        String secondMenuItemValue = jsonParser.parse(json, "menu.popup.menuitem.1.value");
        Assert.assertEquals("Open", secondMenuItemValue);

        String secondMenuItemOnclick = jsonParser.parse(json, "menu.popup.menuitem.1.onclick");
        Assert.assertEquals("OpenDoc()", secondMenuItemOnclick);

        String thirdMenuItemValue = jsonParser.parse(json, "menu.popup.menuitem.2.value");
        Assert.assertEquals("Close", thirdMenuItemValue);

        String thirdMenuItemOnclick = jsonParser.parse(json, "menu.popup.menuitem.2.onclick");
        Assert.assertEquals("CloseDoc()", thirdMenuItemOnclick);
    }
}