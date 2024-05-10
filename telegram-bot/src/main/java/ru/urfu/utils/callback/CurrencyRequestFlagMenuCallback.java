package ru.urfu.utils.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.urfu.controller.constant.Menus;
import ru.urfu.model.CurrencyRequest;

/**
 * {@link CurrencyRequestMenuCallback} опционально содержащий флаг (по умолчанию false)
 */
public class CurrencyRequestFlagMenuCallback extends CurrencyRequestMenuCallback {

    public CurrencyRequestFlagMenuCallback(Menus menu, CurrencyRequest currencyRequest) {
        super(menu, currencyRequest);
    }

    /**
     * Десериализовать опции из {@link CallbackQuery}
     * @param callbackQuery поле {@link org.telegram.telegrambots.meta.api.objects.Update обновления}
     */
    public CurrencyRequestFlagMenuCallback(CallbackQuery callbackQuery) {
        super(callbackQuery);
    }

    /**
     * Инвертировать флаг
     */
    public void invertFlag() {
        if (containsOption(Options.CONTAINS))
            removeOption(Options.CONTAINS);
        else
            addOption(Options.CONTAINS, null);
    }
}
