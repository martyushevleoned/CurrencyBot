package ru.urfu.controller.menu;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.urfu.controller.constant.Menus;

/**
 * Меню вызываемое по нажатию кнопки
 */
public interface CallbackMenu {

    /**
     * Получить уникальную для каждой реализации константу {@link Menus menu}
     */
    Menus getMenu();

    /**
     * Формирует изменённое меню для отправки
     */
    EditMessageText formEditMessage(CallbackQuery callbackQuery);

}