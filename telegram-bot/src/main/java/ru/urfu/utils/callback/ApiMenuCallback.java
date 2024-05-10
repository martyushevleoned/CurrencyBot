package ru.urfu.utils.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.urfu.controller.constant.Menus;
import ru.urfu.exceptions.CallbackException;

/**
 * {@link MenuCallback} обязательно содержащий название API
 */
public class ApiMenuCallback extends MenuCallback {

    public ApiMenuCallback(Menus menu, String apiName) {
        super(menu);
        addOption(Options.API_NAME, apiName);
    }

    /**
     * Десериализовать опции из {@link CallbackQuery}
     * @param callbackQuery поле {@link org.telegram.telegrambots.meta.api.objects.Update обновления}
     * @throws CallbackException если отсутствует опция - название API
     */
    public ApiMenuCallback(CallbackQuery callbackQuery) {
        super(callbackQuery);
        if (!containsOption(Options.API_NAME))
            throw new CallbackException("Отсутствует название API");
    }

    /**
     * Получить название API
     */
    public String getApiName() {
        return getOption(Options.API_NAME);
    }
}
