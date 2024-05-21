package ru.urfu.utils.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.urfu.controller.constant.MenuType;
import ru.urfu.exceptions.CallbackException;

/**
 * {@link Callback} обязательно содержащий название меню
 */
public class MenuCallback extends Callback {

    public MenuCallback(MenuType menu) {
        super();
        addOption(Option.MENU_NAME, menu.getMenuName());
    }

    /**
     * Десериализовать опции из {@link CallbackQuery}
     * @param callbackQuery поле {@link org.telegram.telegrambots.meta.api.objects.Update обновления}
     * @throws CallbackException если отсутствует опция - название меню
     */
    public MenuCallback(CallbackQuery callbackQuery) {
        super(callbackQuery);
        if (!containsOption(Option.MENU_NAME))
            throw new CallbackException("Отсутствует название меню");
    }

    /**
     * Получить название меню
     */
    public String getMenuName() {
        return getOption(Option.MENU_NAME);
    }
}
