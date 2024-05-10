package ru.urfu.utils.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.urfu.controller.constant.Menus;
import ru.urfu.exceptions.CallbackException;

/**
 * {@link MenuCallback} обязательно содержащий номер страницы
 */
public class MultipageMenuCallback extends MenuCallback {

    public MultipageMenuCallback(Menus menu) {
        super(menu);
        addOption(Options.PAGE_INDEX, "0");
    }

    public MultipageMenuCallback(Menus menu, int pageIndex) {
        super(menu);
        addOption(Options.PAGE_INDEX, String.valueOf(pageIndex));
    }

    /**
     * Десериализовать опции из {@link CallbackQuery}
     * @param callbackQuery поле {@link org.telegram.telegrambots.meta.api.objects.Update обновления}
     * @throws CallbackException если отсутствует опция - номер страницы
     */
    public MultipageMenuCallback(CallbackQuery callbackQuery) {
        super(callbackQuery);
        if (!containsOption(Options.PAGE_INDEX))
            throw new CallbackException("Отсутствует номер страницы");
    }

    /**
     * Получить номер страницы
     */
    public int getPageIndex() {
        return Integer.parseInt(getOption(Options.PAGE_INDEX));
    }
}
