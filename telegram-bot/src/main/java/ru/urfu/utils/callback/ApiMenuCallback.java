package ru.urfu.utils.callback;

import ru.urfu.controller.constant.MenuType;
import ru.urfu.exceptions.CallbackException;

/**
 * {@link MenuCallback} обязательно содержащий название API
 */
public class ApiMenuCallback extends MenuCallback {

    /**
     * TODO
     */
    public ApiMenuCallback(MenuType menu, String apiName) {
        super(menu);
        addOption(Option.API_NAME, apiName);
    }

    /**
     * TODO
     */
    public ApiMenuCallback(Callback callback) {
        super(callback);
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
