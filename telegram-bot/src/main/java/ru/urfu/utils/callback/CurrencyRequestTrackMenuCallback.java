package ru.urfu.utils.callback;

import ru.urfu.controller.constant.MenuType;
import ru.urfu.model.CurrencyRequest;

/**
 * {@link CurrencyRequestMenuCallback}  опционально содержащий флаги (по умолчанию false)
 * для добавления/удаления валюты из списка отслеживаемых
 */
public class CurrencyRequestTrackMenuCallback extends CurrencyRequestMenuCallback {

    public CurrencyRequestTrackMenuCallback(MenuType menu, CurrencyRequest currencyRequest) {
        super(menu, currencyRequest);
    }

    /**
     * TODO
     */
    public CurrencyRequestTrackMenuCallback(Callback callback) {
        super(callback);
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
