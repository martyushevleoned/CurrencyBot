package ru.urfu.utils.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.urfu.controller.constant.MenuTypes;
import ru.urfu.exceptions.CallbackException;

/**
 * {@link MenuCallback} обязательно содержащий название API
 */
public class ApiMenuCallback extends MenuCallback {

    public ApiMenuCallback(MenuTypes menu, String apiName) {
        super(menu);
        addOption(Option.API_NAME, apiName);
    }

    /**
     * Десериализовать опции из {@link CallbackQuery}
     * @param callbackQuery поле {@link org.telegram.telegrambots.meta.api.objects.Update обновления}
     * @throws CallbackException если отсутствует опция - название API
     */
    public ApiMenuCallback(CallbackQuery callbackQuery) {
        super(callbackQuery);
        if (!containsOption(Option.API_NAME))
            throw new CallbackException("Отсутствует название API");
    }

    /**
     * Получить название API
     */
    public String getApiName() {
        return getOption(Option.API_NAME);
    }
}
