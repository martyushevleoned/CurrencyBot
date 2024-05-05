package ru.urfu.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Тестирование класса {@link Callback}
 */
@RunWith(MockitoJUnitRunner.class)
public class CallbackTest {

    private Callback callback;

    @Mock
    private CallbackQuery callbackQuery;

    /**
     * Создать {@link Callback} со случайным названием меню
     */
    @Before
    public void setUp() {
        String menuName = RandomStringUtils.random(100);
        callback = new Callback(menuName);
    }

    /**
     * Тестирование методов
     * {@link Callback#addFlag}
     * {@link Callback#removeFlag}
     * {@link Callback#containsFlag}
     * {@link Callback#getFlag}
     * <ul>
     *     <li>Создаёт callback</li>
     *     <li>Добавляет первый флаг в callback</li>
     *     <li>Проверяет наличие и значение первого флага</li>
     *     <li>Добавляет второй флаг в callback</li>
     *     <li>Проверяет наличие и значение второго флага</li>
     *     <li>Удаляет первый флаг из callback</li>
     *     <li>Проверяет отсутствие первого флага</li>
     *     <li>Проверяет отсутствие значения первого флага</li>
     *     <li>Проверяет наличие и значение второго флага</li>
     * </ul>
     */
    @Test
    public void addRemoveContainsGetFlagTest() {

        callback.addFlag("first flag", "first value");
        Assert.assertTrue(callback.containsFlag("first flag"));
        Assert.assertEquals("first value", callback.getFlag("first flag"));

        callback.addFlag("second flag", "second value");
        Assert.assertTrue(callback.containsFlag("second flag"));
        Assert.assertEquals("second value", callback.getFlag("second flag"));

        callback.removeFlag("first flag");
        Assert.assertFalse(callback.containsFlag("first flag"));
        Assert.assertNull(callback.getFlag("first flag"));
        Assert.assertTrue(callback.containsFlag("second flag"));
        Assert.assertEquals("second value", callback.getFlag("second flag"));
    }

    /**
     * Тестирование методов {@link Callback#Callback(CallbackQuery)} и {@link Callback#toString}
     * <ul>
     *     <li>Создаёт baseCallback</li>
     *     <li>Добавляет в baseCallback два флага</li>
     *     <li>Имитирует получение {@link CallbackQuery} из {@link Update}</li>
     *     <li>Создааёт callbackFromString из {@link CallbackQuery}</li>
     *     <li>Проверяет наличие и значения флагов из baseCallback в callbackFromString</li>
     * </ul>
     */
    @Test
    public void createCallbackFromCallbackQueryTest() {

        callback.addFlag("first flag", "first value");
        callback.addFlag("second flag", "second value");

        Mockito.when(callbackQuery.getData()).thenReturn(callback.toString());
        Callback callbackFromString = new Callback(callbackQuery);

        Assert.assertTrue(callbackFromString.containsFlag("first flag"));
        Assert.assertTrue(callbackFromString.containsFlag("second flag"));
        Assert.assertEquals("first value", callbackFromString.getFlag("first flag"));
        Assert.assertEquals("second value", callbackFromString.getFlag("second flag"));
    }
}