package ru.urfu.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.urfu.model.CurrencyRequest;

import java.util.Objects;
import java.util.Set;

/**
 * Тестирование компонента {@link TrackService}
 */
public class TrackServiceTest {

    private static final int RANDOM_STRING_LENGTH = 10;

    private final TrackService trackService = new TrackService();
    private CurrencyRequest firstCurrencyRequest;
    private CurrencyRequest secondCurrencyRequest;

    /**
     * Создать 2 случайных не равных {@link CurrencyRequest сurrencyRequest}
     */
    @Before
    public void setUp() {
        firstCurrencyRequest = new CurrencyRequest(RandomStringUtils.random(RANDOM_STRING_LENGTH), RandomStringUtils.random(RANDOM_STRING_LENGTH));
        secondCurrencyRequest = new CurrencyRequest(RandomStringUtils.random(RANDOM_STRING_LENGTH), RandomStringUtils.random(RANDOM_STRING_LENGTH));

        while (Objects.equals(firstCurrencyRequest, secondCurrencyRequest)) {
            secondCurrencyRequest = new CurrencyRequest(RandomStringUtils.random(RANDOM_STRING_LENGTH), RandomStringUtils.random(RANDOM_STRING_LENGTH));
        }
    }

    /**
     * Тестирование добавление/удаление/наличия валюты в списке отслеживаемых
     * одного конкретного пользователя, методами
     * {@link TrackService#addToTrack}
     * {@link TrackService#removeFromTrack}
     * {@link TrackService#isTracked}
     * <ul>
     *     <li>Проверяет отсутствие firstCurrencyRequest в trackService</li>
     *     <li>Добавляет firstCurrencyRequest в trackService</li>
     *     <li>Проверяет наличие firstCurrencyRequest в trackService</li>
     *     <li>Проверяет состав trackService</li>
     *     <li>Проверяет отсутствие secondCurrencyRequest в trackService</li>
     *     <li>Добавляет secondCurrencyRequest в trackService</li>
     *     <li>Проверяет наличие secondCurrencyRequest в trackService</li>
     *     <li>Проверяет состав trackService</li>
     *     <li>Удаляет {@link CurrencyRequest firstCurrencyRequest} из trackService</li>
     *     <li>Проверяет отсутствие firstCurrencyRequest в trackService</li>
     *     <li>Проверяет наличие secondCurrencyRequest в trackService</li>
     *     <li>Проверяет состав trackService</li>
     * </ul>
     */
    @Test
    public void addRemoveTrackIsTrackedTest() {

        Assert.assertFalse(trackService.isTracked(1, firstCurrencyRequest));
        trackService.addToTrack(1, firstCurrencyRequest);
        Assert.assertTrue(trackService.isTracked(1, firstCurrencyRequest));
        Assert.assertEquals(Set.of(firstCurrencyRequest), trackService.getTrackedRequests(1));

        Assert.assertFalse(trackService.isTracked(1, secondCurrencyRequest));
        trackService.addToTrack(1, secondCurrencyRequest);
        Assert.assertTrue(trackService.isTracked(1, secondCurrencyRequest));
        Assert.assertEquals(Set.of(firstCurrencyRequest, secondCurrencyRequest), trackService.getTrackedRequests(1));

        trackService.removeFromTrack(1, firstCurrencyRequest);
        Assert.assertFalse(trackService.isTracked(1, firstCurrencyRequest));
        Assert.assertTrue(trackService.isTracked(1, secondCurrencyRequest));
        Assert.assertEquals(Set.of(secondCurrencyRequest), trackService.getTrackedRequests(1));
    }

    /**
     * Тестирование добавление валюты в списоки отслеживаемых разных пользователей, методом
     * {@link TrackService#addToTrack}
     * <ul>
     *     <li>Создаёт {@link CurrencyRequest firstCurrencyRequest}</li>
     *     <li>Создаёт {@link CurrencyRequest secondCurrencyRequest}</li>
     *     <li>Добавляет firstCurrencyRequest в список отслеживаемых валют первого пользователя</li>
     *     <li>Добавляет secondCurrencyRequest в список отслеживаемых валют второго пользователя</li>
     *     <li>Проверяет список отслеживаемых валют первого пользователя</li>
     *     <li>Проверяет список отслеживаемых валют второго пользователя</li>
     * </ul>
     */
    @Test
    public void addToTrackDifferentUsersTest() {

        trackService.addToTrack(1, firstCurrencyRequest);
        trackService.addToTrack(2, secondCurrencyRequest);

        Assert.assertEquals(Set.of(firstCurrencyRequest), trackService.getTrackedRequests(1));
        Assert.assertEquals(Set.of(secondCurrencyRequest), trackService.getTrackedRequests(2));
    }

}