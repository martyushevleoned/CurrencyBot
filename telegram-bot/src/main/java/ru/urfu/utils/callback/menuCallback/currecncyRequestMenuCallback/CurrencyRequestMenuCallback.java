package ru.urfu.utils.callback.menuCallback.currecncyRequestMenuCallback;

import ru.urfu.controller.constant.MenuType;
import ru.urfu.exceptions.CallbackException;
import ru.urfu.model.CurrencyRequest;
import ru.urfu.utils.callback.Callback;
import ru.urfu.utils.callback.Option;
import ru.urfu.utils.callback.menuCallback.ApiMenuCallback;

/**
 * {@link ApiMenuCallback} обязательно содержащий {@link CurrencyRequest}
 */
public class CurrencyRequestMenuCallback extends ApiMenuCallback {

    /**
     * Создать сериализуемый объект с обязательными опциями - тип меню и {@link CurrencyRequest}
     *
     * @param menu тип меню
     */
    public CurrencyRequestMenuCallback(MenuType menu, CurrencyRequest currencyRequest) {
        super(menu, currencyRequest.api());
        addOption(Option.CURRENCY_NAME, currencyRequest.currency());
    }

    /**
     * Создать новый объект, приведённый к {@link CurrencyRequestMenuCallback}
     *
     * @param callback объект для приведения
     * @throws CallbackException если в объекте для приведения отсутствует обязательные поля
     */
    public CurrencyRequestMenuCallback(Callback callback) {
        super(callback);
        if (!containsOption(Option.CURRENCY_NAME))
            throw new CallbackException("Отсутствует название валюты");
    }

    /**
     * Получить {@link CurrencyRequest}
     */
    public CurrencyRequest getCurrencyRequest(){
        return new CurrencyRequest(
                getOption(Option.API_NAME),
                getOption(Option.CURRENCY_NAME)
        );
    }
}
