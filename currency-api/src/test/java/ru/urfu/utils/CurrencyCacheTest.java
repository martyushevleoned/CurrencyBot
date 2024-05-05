package ru.urfu.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.urfu.model.CurrencyResponse;

import java.time.Duration;
import java.util.Random;

/**
 * Тестирование класса {@link CurrencyCache}
 */
public class CurrencyCacheTest {

    private static final int RANDOM_STRING_LENGTH = 10;

    private CurrencyCache cache;

    @Before
    public void setUp() {
        cache = new CurrencyCache(Duration.ofSeconds(1));
    }

    /**
     * Тестирование сохранения стоимости валюты в кэш.
     * <ul>
     *     <li>Генерирует случайное название валюты и стоимость</li>
     *     <li>Добавляет их в кэш</li>
     *     <li>Проверяет наличие валюты в кэше</li>
     *     <li>Проверяет полученное значение из кэша с изначальным</li>
     * </ul>
     */
    @Test
    public void saveGetTest() {

        String currencyName = RandomStringUtils.random(RANDOM_STRING_LENGTH);
        double currencyPrice = new Random().nextDouble();

        cache.save(currencyName, currencyPrice);

        Assert.assertTrue(cache.contains(currencyName));
        Assert.assertFalse(cache.notContains(currencyName));

        CurrencyResponse currencyResponse = cache.get(currencyName);
        Assert.assertEquals(currencyPrice, currencyResponse.getPrice(), 1e-5);
    }

    /**
     * Тестирование инвалидации стоимости валюты из кэша при превышении периода обновления стоимости.
     * <ul>
     *     <li>Добавляет валюту в кэш</li>
     *     <li>Ждёт 950 мс (чуть меньше периода обновления валюты)</li>
     *     <li>Проверяет наличие валюты в кэше</li>
     *     <li>Ждёт 70 мс</li>
     *     <li>Проверяет отсутствие валюты в кэше</li>
     * </ul>
     */
    @Test
    public void timeInvalidationTest() throws InterruptedException {

        cache.save("currency", 0);

        Thread.sleep(950);
        Assert.assertTrue(cache.contains("currency"));

        Thread.sleep(70);
        Assert.assertFalse(cache.contains("currency"));
    }
}