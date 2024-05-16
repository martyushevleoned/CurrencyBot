package ru.urfu.utils.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.urfu.controller.constant.MenuTypes;
import ru.urfu.exceptions.CallbackException;
import ru.urfu.model.CurrencyRequest;

/**
 * {@link ApiMenuCallback} обязательно содержащий {@link CurrencyRequest}
 */
public class CurrencyRequestMenuCallback extends ApiMenuCallback {

    public CurrencyRequestMenuCallback(MenuTypes menu, CurrencyRequest currencyRequest) {
        super(menu, currencyRequest.api());
        addOption(Option.CURRENCY_NAME, currencyRequest.currency());
    }

    /**
     * Десериализовать опции из {@link CallbackQuery}
     * @param callbackQuery поле {@link org.telegram.telegrambots.meta.api.objects.Update обновления}
     * @throws CallbackException если отсутствует опция - название валюты
     */
    public CurrencyRequestMenuCallback(CallbackQuery callbackQuery) {
        super(callbackQuery);
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
