package ru.urfu.utils.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.urfu.controller.constant.MenuTypes;
import ru.urfu.model.CurrencyRequest;

/**
 * {@link CurrencyRequestMenuCallback}  опционально содержащий флаги (по умолчанию false)
 * для добавления/удаления валюты из списка отслеживаемых
 */
public class CurrencyRequestTrackMenuCallback extends CurrencyRequestMenuCallback {

    public CurrencyRequestTrackMenuCallback(MenuTypes menu, CurrencyRequest currencyRequest) {
        super(menu, currencyRequest);
    }

    /**
     * Десериализовать опции из {@link CallbackQuery}
     * @param callbackQuery поле {@link org.telegram.telegrambots.meta.api.objects.Update обновления}
     */
    public CurrencyRequestTrackMenuCallback(CallbackQuery callbackQuery) {
        super(callbackQuery);
    }

    /**
     * Добавить флаг для добавления валюты в список отслеживаемых
     */
    public void addAddToTrackFlag() {
        addOption(Option.ADD_TO_TRACK, null);
    }

    /**
     * Добавить флаг для удаления валюты из списка отслеживаемых
     */
    public void addRemoveFromTrackFlag() {
        addOption(Option.REMOVE_FROM_TRACK, null);
    }

    /**
     * Есть ли флаг добавления валюты в список отслеживаемых
     */
    public boolean isAddToTrack() {
        return containsOption(Option.ADD_TO_TRACK);
    }

    /**
     * Есть ли флаг удаления валюты из списка отслеживаемых
     */
    public boolean isRemoveFromTrack() {
        return containsOption(Option.REMOVE_FROM_TRACK);
    }
}
