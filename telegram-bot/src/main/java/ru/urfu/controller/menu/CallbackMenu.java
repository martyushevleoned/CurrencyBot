package ru.urfu.controller.menu;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

/**
 * Меню вызываемое по нажатию кнопки
 */
public interface CallbackMenu extends Menu {

    /**
     * Вызывает ли нажатие кнопки данное меню
     */
    boolean matchEditMessage(CallbackQuery callbackQuery);

    /**
     * Формирует изменённое меню для отправки
     */
    EditMessageText formEditMessage(CallbackQuery callbackQuery);

}