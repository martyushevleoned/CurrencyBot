package ru.urfu.utils.callback;

import ru.urfu.controller.constant.MenuType;
import ru.urfu.exceptions.CallbackException;

/**
 * {@link MenuCallback} обязательно содержащий номер страницы
 */
public class MultipageMenuCallback extends MenuCallback {

    /**
     * TODO
     */
    public MultipageMenuCallback(MenuType menu) {
        super(menu);
        addOption(Option.PAGE_INDEX, "0");
    }

    /**
     * TODO
     */
    public MultipageMenuCallback(MenuType menu, int pageIndex) {
        super(menu);
        addOption(Option.PAGE_INDEX, String.valueOf(pageIndex));
    }

    /**
     * TODO
     */
    public MultipageMenuCallback(Callback callback) {
        super(callback);
        if (!containsOption(Option.PAGE_INDEX))
            throw new CallbackException("Отсутствует номер страницы");
    }

    /**
     * Получить номер страницы
     */
    public int getPageIndex() {
        return Integer.parseInt(getOption(Option.PAGE_INDEX));
    }
}
