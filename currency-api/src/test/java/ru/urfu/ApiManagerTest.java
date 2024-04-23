package ru.urfu;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ru.urfu.api.CurrencyApi;
import ru.urfu.exceptions.ApiNotFoundException;
import ru.urfu.exceptions.ApiNotSupportedCurrencyException;
import ru.urfu.model.CurrencyRequest;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Тестирование класса {@link ApiManager}
 */
@RunWith(MockitoJUnitRunner.class)
public class ApiManagerTest {

    private static final int RANDOM_STRING_LENGTH = 10;

    private ApiManager apiManager;

    @Mock
    private CurrencyApi existedCurrencyApi;

    private String apiName;
    private String currencyName;

    private String corruptedApiName;
    private String corruptedCurrencyName;

    /**
     * Создать {@link ApiManager} с одной подключённой валютой
     */
    @Before
    public void setUp() {
        apiName = RandomStringUtils.random(RANDOM_STRING_LENGTH);
        currencyName = RandomStringUtils.random(RANDOM_STRING_LENGTH);

        corruptedApiName = RandomStringUtils.random(RANDOM_STRING_LENGTH);
        corruptedCurrencyName = RandomStringUtils.random(RANDOM_STRING_LENGTH);

        while (Objects.equals(apiName, corruptedApiName) && Objects.equals(currencyName, corruptedCurrencyName)) {
            corruptedApiName = RandomStringUtils.random(RANDOM_STRING_LENGTH);
            corruptedCurrencyName = RandomStringUtils.random(RANDOM_STRING_LENGTH);
        }

        Mockito.when(existedCurrencyApi.getName()).thenReturn(apiName);
        Mockito.when(existedCurrencyApi.getCurrencies()).thenReturn(Set.of(currencyName));

        apiManager = new ApiManager(List.of(existedCurrencyApi));
    }

    /**
     * Тестирование метода {@link ApiManager#getAllApiNames} при одном подключённом API
     */
    @Test
    public void getAllApiNames() {
        Assert.assertEquals(Set.of(apiName), apiManager.getAllApiNames());
    }

    /**
     * Тестирование метода {@link ApiManager#getPossibleRequests} при одном подключённом API,
     * поддерживающим получение стоимости одной валлюты
     */
    @Test
    public void getPossibleRequests() {
        Assert.assertEquals(Set.of(new CurrencyRequest(apiName, currencyName)), apiManager.getPossibleRequests());
    }

    /**
     * Тестирование вызова исключений при попытке получения стоимости несуществующего {@link CurrencyRequest запроса}
     * <ul>
     *     <li>Создаёт запрос с недопустимым названием API</li>
     *     <li>Проверяет вызов {@link ApiNotFoundException имключения}</li>
     *     <li>Проверяет сообщение исключения</li>
     *     <li>Создаёт запрос с недопустимым названием валюты</li>
     *     <li>Проверяет вызов {@link ApiNotSupportedCurrencyException имключения}</li>
     *     <li>Проверяет сообщение исключения</li>
     *     <li>Создаёт запрос с недопустимым названием API и валюты</li>
     *     <li>Проверяет вызов {@link ApiNotFoundException имключения}</li>
     *     <li>Проверяет сообщение исключения</li>
     * </ul>
     */
    @Test
    public void getCorruptedPriceTest() {

        CurrencyRequest corruptedApiNameRequest = new CurrencyRequest(corruptedApiName, currencyName);
        ApiNotFoundException apiNotFoundException = Assert.assertThrows(
                ApiNotFoundException.class,
                () -> apiManager.getPrice(corruptedApiNameRequest));
        Assert.assertEquals("Указанный API не существует", apiNotFoundException.getMessage());

        CurrencyRequest corruptedCurrencyNameRequest = new CurrencyRequest(apiName, corruptedCurrencyName);
        ApiNotSupportedCurrencyException apiNotSupportedCurrencyException = Assert.assertThrows(
                ApiNotSupportedCurrencyException.class,
                () -> apiManager.getPrice(corruptedCurrencyNameRequest));
        Assert.assertEquals("API не поддерживает данную валюту", apiNotSupportedCurrencyException.getMessage());

        CurrencyRequest corruptedApiAndCurrencyNameRequest = new CurrencyRequest(corruptedApiName, corruptedCurrencyName);
        ApiNotFoundException exception = Assert.assertThrows(
                ApiNotFoundException.class,
                () -> apiManager.getPrice(corruptedApiAndCurrencyNameRequest));
        Assert.assertEquals("Указанный API не существует", exception.getMessage());
    }

    /**
     * Тестирование вызова {@link ApiNotFoundException исключения},
     * при попытке получить описание несуществующего API
     * <ul>
     *     <li>Запрашивает описание несуществующего API</li>
     *     <li>Проверяет вызов {@link ApiNotFoundException исключения}</li>
     *     <li>Проверяет сообщение исключения</li>
     * </ul>
     */
    @Test
    public void getCorruptedDescriptionTest() {

        ApiNotFoundException apiNotFoundException = Assert.assertThrows(
                ApiNotFoundException.class,
                () -> apiManager.getDescription("corrupted API name"));
        Assert.assertEquals("Указанный API не существует", apiNotFoundException.getMessage());
    }
}