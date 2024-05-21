package ru.urfu.utils.callback;

/**
 * {@link CurrencyRequestMenuCallback} опционально содержащий флаг (по умолчанию false)
 */
public class CurrencyRequestFlagMenuCallback extends CurrencyRequestMenuCallback {

    /**
     * TODO
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
