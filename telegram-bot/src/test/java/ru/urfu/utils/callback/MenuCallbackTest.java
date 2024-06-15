package ru.urfu.utils.callback;

import org.junit.Assert;
import org.junit.Test;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.urfu.controller.constant.MenuType;
import ru.urfu.model.CurrencyRequest;
import ru.urfu.utils.callback.menuCallback.ApiMenuCallback;
import ru.urfu.utils.callback.menuCallback.MenuCallback;
import ru.urfu.utils.callback.menuCallback.currecncyRequestMenuCallback.CurrencyRequestMenuCallback;

/**
 * Тестирование класса {@link MenuCallback}
 */
public class MenuCallbackTest {

    /**
     * Тестирование сериализации методом {@link MenuCallback#getData()} и десериализации конструктором {@link MenuCallback#MenuCallback(CallbackQuery)}
     * <ul>
     *     <li>Создаёт {@link MenuCallback} с названием меню</li>
     *     <li>Проверяет соответствие названия меню в {@link MenuCallback#getMenuName()}</li>
     *     <li>Имитирует получение {@link CallbackQuery} из {@link org.telegram.telegrambots.meta.api.objects.Update Update}</li>
     *     <li>Создаёт callbackFromString из {@link CallbackQuery}</li>
     *     <li>Проверяет соответствие названия меню в callbackFromString</li>
     * </ul>
     */
    @Test
    public void getMenuNameTest() {

        MenuCallback menuCallback = new MenuCallback(MenuType.MAIN_MENU);
        Assert.assertEquals(MenuType.MAIN_MENU.getMenuName(), menuCallback.getMenuName());

        CallbackQuery callbackQuery = new CallbackQuery(null, null, null, null, menuCallback.getData(), null, null);
        MenuCallback menuCallbackFromString = new MenuCallback(callbackQuery);

        Assert.assertEquals(MenuType.MAIN_MENU.getMenuName(), menuCallbackFromString.getMenuName());
    }

    /**
     * Тестирование получения названия меню из классов - наследников {@link MenuCallback}
     * <ul>
     *     <li>Создаёт {@link ApiMenuCallback} с названием меню и названием API</li>
     *     <li>Имитирует получение {@link CallbackQuery} из {@link org.telegram.telegrambots.meta.api.objects.Update Update}</li>
     *     <li>Создаёт {@link MenuCallback} из {@link CallbackQuery}</li>
     *     <li>Проверяет соответствие названия меню</li>
     *     <li>Создаёт {@link CurrencyRequestMenuCallback} с названием меню и {@link CurrencyRequest}</li>
     *     <li>Имитирует получение {@link CallbackQuery} из {@link org.telegram.telegrambots.meta.api.objects.Update Update}</li>
     *     <li>Создаёт {@link MenuCallback} из {@link CallbackQuery}</li>
     *     <li>Проверяет соответствие названия меню</li>
     * </ul>
     */
    @Test
    public void getChildMenuNameTest() {

        ApiMenuCallback apiMenuCallback = new ApiMenuCallback(MenuType.API_LIST, "api name");
        CallbackQuery callbackQuery = new CallbackQuery(null, null, null, null, apiMenuCallback.getData(), null, null);

        MenuCallback menuCallbackFromString = new MenuCallback(callbackQuery);
        Assert.assertEquals(MenuType.API_LIST.getMenuName(), menuCallbackFromString.getMenuName());

        CurrencyRequestMenuCallback currencyRequestMenuCallback = new CurrencyRequestMenuCallback(MenuType.TRACKED_CURRENCY, new CurrencyRequest("api", "currency"));
        callbackQuery = new CallbackQuery(null, null, null, null, currencyRequestMenuCallback.getData(), null, null);

        menuCallbackFromString = new MenuCallback(callbackQuery);
        Assert.assertEquals(MenuType.TRACKED_CURRENCY.getMenuName(), menuCallbackFromString.getMenuName());
    }
}