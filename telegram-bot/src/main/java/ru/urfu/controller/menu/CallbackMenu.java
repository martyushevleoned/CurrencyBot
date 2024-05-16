package ru.urfu.controller.menu;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.urfu.controller.constant.MenuTypes;

/**
 * Меню вызываемое по нажатию кнопки
 */
public interface CallbackMenu {

    /**
     * Получить {@link MenuTypes тип меню}
     */
    MenuTypes getMenuType();

    /**
     * Формирует изменённое меню для отправки
     */
    EditMessageText formEditMessage(CallbackQuery callbackQuery);

}