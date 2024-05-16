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

        Assert.assertEquals("litecoin", jsonParser.parse(json, "$.data.id"));
        Assert.assertEquals("LTC", jsonParser.parse(json, "$.data.symbol"));
        Assert.assertEquals("crypto", jsonParser.parse(json, "$.data.type"));
        Assert.assertEquals("null", jsonParser.parse(json, "$.data.currencySymbol"));
        Assert.assertEquals(82.5696619464591515, jsonParser.parseDouble(json, "$.data.rateUsd"), 1e-16);
        Assert.assertEquals(1713616056895D, jsonParser.parseDouble(json, "$.timestamp"), 1e-1);
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

        Assert.assertEquals("New", jsonParser.parse(json, "$.menu.popup.menuitem.[0].value"));
        Assert.assertEquals("CreateNewDoc()", jsonParser.parse(json, "$.menu.popup.menuitem.[0].onclick"));
        Assert.assertEquals("Open", jsonParser.parse(json, "$.menu.popup.menuitem.[1].value"));
        Assert.assertEquals("OpenDoc()", jsonParser.parse(json, "$.menu.popup.menuitem.[1].onclick"));
        Assert.assertEquals("Close", jsonParser.parse(json, "$.menu.popup.menuitem.[2].value"));
        Assert.assertEquals("CloseDoc()", jsonParser.parse(json, "$.menu.popup.menuitem.[2].onclick"));
    }
}