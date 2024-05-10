package ru.urfu.utils.callback.menuCallback;

import ru.urfu.controller.constant.MenuType;
import ru.urfu.exceptions.CallbackException;
import ru.urfu.utils.callback.Callback;
import ru.urfu.utils.callback.Option;

/**
 * {@link MenuCallback} обязательно содержащий название API
 */
public class ApiMenuCallback extends MenuCallback {

    /**
     * Создать сериализуемый объект с обязательными опциями - тип меню и название API
     *
     * @param menu тип меню
     */
    public ApiMenuCallback(MenuType menu, String apiName) {
        super(menu);
        addOption(Option.API_NAME, apiName);
    }

    /**
     * Создать новый объект, приведённый к {@link ApiMenuCallback}
     *
     * @param callback объект для приведения
     * @throws CallbackException если в объекте для приведения отсутствует обязательные поля
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
