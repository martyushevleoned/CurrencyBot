package ru.urfu.utils.callback;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.urfu.exceptions.CallbackException;

/**
 * Тестирование класса {@link Callback}
 */
public class CallbackTest {

    private Callback callback;

    @Before
    public void setUp() {
        callback = new Callback();
    }

    /**
     * Тестирование методов
     * {@link Callback#addOption}
     * {@link Callback#removeOption}
     * {@link Callback#containsOption}
     * {@link Callback#getOption}
     * <ul>
     *     <li>Создаёт callback</li>
     *     <li>Добавляет опцию API_NAME в callback</li>
     *     <li>Проверяет наличие и значение опции API_NAME</li>
     *     <li>Добавляет опцию CURRENCY_NAME в callback</li>
     *     <li>Проверяет наличие и значение опции CURRENCY_NAME</li>
     *     <li>Удаляет опцию API_NAME из callback</li>
     *     <li>Проверяет отсутствие опции API_NAME</li>
     *     <li>Проверяет вызов исключения при получении значения опции API_NAME</li>
     *     <li>Проверяет наличие и значение опции CURRENCY_NAME</li>
     * </ul>
     */
    @Test
    public void addRemoveContainsGetOptionTest() {

        callback.addOption(Option.API_NAME, "api name");
        Assert.assertTrue(callback.containsOption(Option.API_NAME));
        Assert.assertEquals("api name", callback.getOption(Option.API_NAME));

        callback.addOption(Option.CURRENCY_NAME, "currency name");
        Assert.assertTrue(callback.containsOption(Option.CURRENCY_NAME));
        Assert.assertEquals("currency name", callback.getOption(Option.CURRENCY_NAME));

        callback.removeOption(Option.API_NAME);
        Assert.assertFalse(callback.containsOption(Option.API_NAME));

        CallbackException e = Assert.assertThrows(CallbackException.class, () -> callback.getOption(Option.API_NAME));
        Assert.assertEquals("Опция API_NAME отсутствует", e.getMessage());

        Assert.assertTrue(callback.containsOption(Option.CURRENCY_NAME));
        Assert.assertEquals("currency name", callback.getOption(Option.CURRENCY_NAME));
    }

    /**
     * Тестирование сериализации методом {@link Callback#getData()} и десериализации конструктором {@link Callback#Callback(CallbackQuery)}
     * <ul>
     *     <li>Добавляет в callback две опции</li>
     *     <li>Имитирует получение {@link CallbackQuery} из {@link org.telegram.telegrambots.meta.api.objects.Update Update}</li>
     *     <li>Создаёт callbackFromString из {@link CallbackQuery}</li>
     *     <li>Проверяет наличие и значения опций в callbackFromString</li>
     * </ul>
     */
    @Test
    public void createCallbackFromCallbackQueryTest() {

        callback.addOption(Option.MENU_NAME, "menu name");
        callback.addOption(Option.CURRENCY_NAME, "currency name");

        CallbackQuery callbackQuery = new CallbackQuery(null, null, null, null, callback.getData(), null, null);
        Callback callbackFromString = new Callback(callbackQuery);

        Assert.assertTrue(callbackFromString.containsOption(Option.MENU_NAME));
        Assert.assertTrue(callbackFromString.containsOption(Option.CURRENCY_NAME));

        Assert.assertEquals("menu name", callbackFromString.getOption(Option.MENU_NAME));
        Assert.assertEquals("currency name", callbackFromString.getOption(Option.CURRENCY_NAME));
    }
}