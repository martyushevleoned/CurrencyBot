package ru.urfu.utils.callback.menuCallback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.urfu.controller.constant.MenuType;
import ru.urfu.exceptions.CallbackException;
import ru.urfu.utils.callback.Callback;
import ru.urfu.utils.callback.Option;

/**
 * {@link Callback} обязательно содержащий название меню
 */
public class MenuCallback extends Callback {

    /**
     * Создать сериализуемый объект с опцией - тип меню
     *
     * @param menu тип меню
     */
    public MenuCallback(MenuType menu) {
        super();
        addOption(Option.MENU_NAME, menu.getMenuName());
    }

    /**
     * Десериализовать из {@link CallbackQuery}
     *
     * @param callbackQuery поле {@link org.telegram.telegrambots.meta.api.objects.Update обновления}
     * @throws CallbackException если отсутствует опция - название меню
     */
    public MenuCallback(CallbackQuery callbackQuery) {
        super(callbackQuery);
        if (!containsOption(Option.MENU_NAME))
            throw new CallbackException("Отсутствует название меню");
    }

    /**
     * Создать новый объект, приведённый к {@link MenuCallback}
     *
     * @param callback объект для приведения
     * @throws CallbackException если в объекте для приведения отсутствует обязательное поле - название меню
     */
    public MenuCallback(Callback callback) {
        super(callback);
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
