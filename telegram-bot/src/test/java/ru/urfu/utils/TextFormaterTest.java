package ru.urfu.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.urfu.model.CurrencyRequest;
import ru.urfu.model.CurrencyResponse;

import java.util.Date;
import java.util.Random;

/**
 * Тестирование компонента {@link TextFormater}
 */
public class TextFormaterTest {

    private final TextFormater textFormater = new TextFormater();
    private CurrencyRequest currencyRequest;
    private CurrencyResponse currencyResponse;

    private String apiName;
    private String currencyName;
    private double price;

    /**
     * Создать случайный {@link CurrencyRequest} и {@link CurrencyResponse}
     */
    @Before
    public void setUp() {

        apiName = RandomStringUtils.random(100);
        currencyName = RandomStringUtils.random(100);
        price = new Random().nextDouble();

        currencyRequest = new CurrencyRequest(apiName, currencyName);
        currencyResponse = new CurrencyResponse(price, new Date());
    }

    /**
     * Тестирование метода {@link TextFormater#getCurrencyInfo} на соответствие шаблону,
     * для случайных данных.
     * <ul>
     *     <li>Создаёт случайное название API</li>
     *     <li>Создаёт случайное название валюты</li>
     *     <li>Создаёт {@link CurrencyRequest} с соответствующими параметрами</li>
     *     <li>Проверяет соответствие шаблону</li>
     * </ul>
     */
    @Test
    public void getRandomCurrencyInfoTest() {

        CurrencyRequest currencyRequest = new CurrencyRequest(apiName, currencyName);
        Assert.assertEquals("%s - %s".formatted(apiName, currencyName), textFormater.getCurrencyInfo(currencyRequest));
    }

    /**
     * Тестирование метода {@link TextFormater#getPriceInfo} на соответствие шаблону,
     * для случайных данных.
     * <ul>
     *     <li>Создаёт случайное название API</li>
     *     <li>Создаёт случайное название валюты</li>
     *     <li>Создаёт случайную стоимость валюты</li>
     *     <li>Создаёт {@link CurrencyRequest} и {@link CurrencyResponse} с соответствующими параметрами</li>
     *     <li>Проверяет соответствие шаблону</li>
     * </ul>
     */
    @Test
    public void getRandomPriceInfoTest() {

        String[] messageText = textFormater.getPriceInfo(currencyRequest, currencyResponse).split("\n");
        Assert.assertEquals("API: " + apiName, messageText[0]);
        Assert.assertTrue(messageText[1].startsWith("дата: "));
        Assert.assertTrue(messageText[2].startsWith("время: "));
        Assert.assertEquals("1 %s = %.3f USD".formatted(currencyName, price), messageText[3]);
    }
}