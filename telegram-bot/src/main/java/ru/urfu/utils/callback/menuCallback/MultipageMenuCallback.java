package ru.urfu.utils.callback.menuCallback;

import ru.urfu.controller.constant.MenuType;
import ru.urfu.exceptions.CallbackException;
import ru.urfu.utils.callback.Callback;
import ru.urfu.utils.callback.Option;

/**
 * {@link MenuCallback} обязательно содержащий номер страницы
 */
public class MultipageMenuCallback extends MenuCallback {

    /**
     * Создать сериализуемый объект с обязательными опциями - тип меню и номер страницы.
     * Номер страницы по умолчанию равен нулю.
     *
     * @param menu тип меню
     */
    public MultipageMenuCallback(MenuType menu) {
        super(menu);
        addOption(Option.PAGE_INDEX, "0");
    }

    /**
     * Создать сериализуемый объект с обязательными опциями - тип меню и номер страницы
     *
     * @param menu      тип меню
     * @param pageIndex номер страницы
     */
    public MultipageMenuCallback(MenuType menu, int pageIndex) {
        super(menu);
        addOption(Option.PAGE_INDEX, String.valueOf(pageIndex));
    }

    /**
     * Создать новый объект, приведённый к {@link MultipageMenuCallback}
     *
     * @param callback объект для приведения
     * @throws CallbackException если в объекте для приведения отсутствует обязательные поля
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
