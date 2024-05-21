package ru.urfu.utils.callback;

import ru.urfu.controller.constant.MenuType;
import ru.urfu.exceptions.CallbackException;
import ru.urfu.model.CurrencyRequest;

/**
 * {@link ApiMenuCallback} обязательно содержащий {@link CurrencyRequest}
 */
public class CurrencyRequestMenuCallback extends ApiMenuCallback {

    public CurrencyRequestMenuCallback(MenuType menu, CurrencyRequest currencyRequest) {
        super(menu, currencyRequest.api());
        addOption(Option.CURRENCY_NAME, currencyRequest.currency());
    }

    /**
     * TODO
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
