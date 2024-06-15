package ru.urfu.controller.menu;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.urfu.controller.constant.MenuType;
import ru.urfu.utils.callback.menuCallback.MenuCallback;

/**
 * Меню вызываемое по нажатию кнопки
 */
public interface CallbackMenu {

    /**
     * Получить {@link MenuType тип меню}
     */
    MenuType getMenuType();

    /**
     * Формирует изменённое меню для отправки
     */
    EditMessageText formEditMessage(long chatId, int messageId, MenuCallback menuCallback);

}