package ru.urfu.utils.callback.menuCallback.currecncyRequestMenuCallback;

import ru.urfu.exceptions.CallbackException;
import ru.urfu.utils.callback.Callback;
import ru.urfu.utils.callback.Option;

/**
 * {@link CurrencyRequestMenuCallback} опционально содержащий флаг (по умолчанию false)
 */
public class CurrencyRequestFlagMenuCallback extends CurrencyRequestMenuCallback {

    /**
     * Создать новый объект, приведённый к {@link CurrencyRequestFlagMenuCallback}
     *
     * @param callback объект для приведения
     * @throws CallbackException если в объекте для приведения отсутствует обязательные поля
     */
    public CurrencyRequestFlagMenuCallback(Callback callback) {
        super(callback);
    }

    /**
     * Инвертировать флаг
     */
    public void invertFlag() {
        if (containsOption(Option.CONTAINS))
            removeOption(Option.CONTAINS);
        else
            addOption(Option.CONTAINS, null);
    }
}
