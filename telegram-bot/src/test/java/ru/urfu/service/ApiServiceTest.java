package ru.urfu.service;

import org.junit.Assert;
import org.junit.Test;
import ru.urfu.model.CurrencyRequest;
import ru.urfu.model.CurrencyResponse;

import java.util.Set;

/**
 * Тестирование компонента {@link ApiService} на работоспособность всех подключённых API
 */
public class ApiServiceTest {

    private final ApiService apiService = new ApiService();

    /**
     * Тестирование корректности получения всех доступных валют методом {@link ApiService#getPossibleRequests}
     * и их стоимости методом {@link ApiService#getPrice}.<br>
     * <b>Данный тест не может быть пройден без подключения к интернету.<br>
     * Рекомендуется не запускать слишком часто данный тест
     * во избежание блокировки со стороны используемых публичных API.
     * </b>
     * <ul>
     *     <li>Получает все доступные {@link CurrencyRequest запросы}</li>
     *     <li>Проверяет наличие ответа от  каждого запроса</li>
     * </ul>
     */
    @Test
    public void getPriceTest() {

        Set<CurrencyRequest> possibleRequests = apiService.getPossibleRequests();

        possibleRequests.forEach(currencyRequest -> {
            CurrencyResponse currencyResponse = apiService.getPrice(currencyRequest);
            Assert.assertNotNull(currencyResponse);
        });
    }

    /**
     * Тестирование получения описания API методом {@link ApiService#getDescription}
     * </b>
     * <ul>
     *     <li>Получает всех доступных API методом {@link ApiService#getAllApiNames}</li>
     *     <li>Проверяет описание на соответствие шаблону</li>
     * </ul>
     */
    @Test
    public void getDescriptionTest() {

        Set<String> apiNames = apiService.getAllApiNames();

        apiNames.forEach(apiName -> {
            String description = apiService.getDescription(apiName);
            String[] parts = description.split("\n");

            Assert.assertTrue(parts[0].startsWith("API: "));
            Assert.assertTrue(parts[1].startsWith("Оффициальный сайт: "));
            Assert.assertTrue(parts[2].startsWith("Период обновления курса: "));
        });
    }
}