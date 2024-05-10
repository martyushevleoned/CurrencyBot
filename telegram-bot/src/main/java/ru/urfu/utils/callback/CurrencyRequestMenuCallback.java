package ru.urfu.utils.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.urfu.controller.constant.Menus;
import ru.urfu.exceptions.CallbackException;
import ru.urfu.model.CurrencyRequest;

/**
 * {@link ApiMenuCallback} обязательно содержащий {@link CurrencyRequest}
 */
public class CurrencyRequestMenuCallback extends ApiMenuCallback {

    public CurrencyRequestMenuCallback(Menus menu, CurrencyRequest currencyRequest) {
        super(menu, currencyRequest.api());
        addOption(Options.CURRENCY_NAME, currencyRequest.currency());
    }

    /**
     * Десериализовать опции из {@link CallbackQuery}
     * @param callbackQuery поле {@link org.telegram.telegrambots.meta.api.objects.Update обновления}
     * @throws CallbackException если отсутствует опция - название валюты
     */
    public CurrencyRequestMenuCallback(CallbackQuery callbackQuery) {
        super(callbackQuery);
        if (!containsOption(Options.CURRENCY_NAME))
            throw new CallbackException("Отсутствует название валюты");
    }

    /**
     * Получить {@link CurrencyRequest}
     */
    public CurrencyRequest getCurrencyRequest(){
        return new CurrencyRequest(
                getOption(Options.API_NAME),
                getOption(Options.CURRENCY_NAME)
        );
    }
}
