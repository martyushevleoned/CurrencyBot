package ru.urfu;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import ru.urfu.api.CurrencyApi;
import ru.urfu.exceptions.ApiNotFoundException;
import ru.urfu.exceptions.ApiNotSupportedCurrencyException;
import ru.urfu.model.CurrencyRequest;

import java.util.List;
import java.util.Set;

/**
 * Тестирование класса {@link ApiService}
 */
public class ApiServiceTest {

    private final ApiService apiService;

    /**
     * Инициализировать {@link ApiService} с двумя подключенными API
     */
    public ApiServiceTest() {

        CurrencyApi firstCurrencyApi = Mockito.mock(CurrencyApi.class);
        Mockito.when(firstCurrencyApi.getName()).thenReturn("Crypto API");
        Mockito.when(firstCurrencyApi.getCurrencies()).thenReturn(Set.of("Bitcoin", "Ethereum"));

        CurrencyApi secondCurrencyApi = Mockito.mock(CurrencyApi.class);
        Mockito.when(secondCurrencyApi.getName()).thenReturn("Fiat API");
        Mockito.when(secondCurrencyApi.getCurrencies()).thenReturn(Set.of("USD", "Euro", "Ruble"));

        this.apiService = new ApiService(List.of(firstCurrencyApi, secondCurrencyApi));
    }

    /**
     * Тестирование метода {@link ApiService#getAllApiNames}
     * <ul>
     *     <li>Создаём список всех поддерживаемых</li>
     *     <li>Сверяем список с результатом {@link ApiService#getAllApiNames}</li>
     * </ul>
     */
    @Test
    public void getAllApiNamesTest() {
        Assert.assertEquals(Set.of("Crypto API", "Fiat API"), apiService.getAllApiNames());
    }

    /**
     * Тестирование метода {@link ApiService#getPossibleRequests}
     * <ul>
     *     <li>Создаём список всех возможных запросов к {@link ApiService}</li>
     *     <li>Сверяем список с результатом {@link ApiService#getPossibleRequests}</li>
     * </ul>
     */
    @Test
    public void getPossibleRequestsTest() {
        Set<CurrencyRequest> requests = Set.of(
                new CurrencyRequest("Crypto API", "Bitcoin"),
                new CurrencyRequest("Crypto API", "Ethereum"),
                new CurrencyRequest("Fiat API", "USD"),
                new CurrencyRequest("Fiat API", "Euro"),
                new CurrencyRequest("Fiat API", "Ruble")
        );
        Assert.assertEquals(requests, apiService.getPossibleRequests());
    }

    /**
     * Тестирование вызова исключений при попытке получения стоимости несуществующего {@link CurrencyRequest запроса}
     * <ul>
     *     <li>Создаёт запрос с недопустимым названием API</li>
     *     <li>Проверяет вызов {@link ApiNotFoundException иcключения}</li>
     *     <li>Проверяет, что сообщение исключения - Указанный API не существует</li>
     *     <li>Создаёт запрос с недопустимым названием валюты</li>
     *     <li>Проверяет вызов {@link ApiNotSupportedCurrencyException иcключения}</li>
     *     <li>Проверяет, что сообщение исключения - API не поддерживает данную валюту</li>
     *     <li>Создаёт запрос с недопустимым названием API и валюты</li>
     *     <li>Проверяет вызов {@link ApiNotFoundException иcключения}</li>
     *     <li>Проверяет, что сообщение исключения - Указанный API не существует</li>
     * </ul>
     */
    @Test
    public void getCorruptedPriceTest() {

        CurrencyRequest corruptedApiNameRequest = new CurrencyRequest("corrupted API name", "Bitcoin");
        ApiNotFoundException apiNotFoundException = Assert.assertThrows(
                ApiNotFoundException.class,
                () -> apiService.getPrice(corruptedApiNameRequest));
        Assert.assertEquals("Указанный API не существует", apiNotFoundException.getMessage());

        CurrencyRequest corruptedCurrencyNameRequest = new CurrencyRequest("Crypto API", "corrupted currency name");
        ApiNotSupportedCurrencyException apiNotSupportedCurrencyException = Assert.assertThrows(
                ApiNotSupportedCurrencyException.class,
                () -> apiService.getPrice(corruptedCurrencyNameRequest));
        Assert.assertEquals("API не поддерживает данную валюту", apiNotSupportedCurrencyException.getMessage());

        CurrencyRequest corruptedApiAndCurrencyNameRequest = new CurrencyRequest("corrupted API name", "corrupted currency name");
        ApiNotFoundException exception = Assert.assertThrows(
                ApiNotFoundException.class,
                () -> apiService.getPrice(corruptedApiAndCurrencyNameRequest));
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
                () -> apiService.getDescription("corrupted API name"));
        Assert.assertEquals("Указанный API не существует", apiNotFoundException.getMessage());
    }
}