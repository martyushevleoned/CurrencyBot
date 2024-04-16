package ru.urfu.controller.menu;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Меню бота - текстовое сообщение (опционально) с кнопками
 */
public interface Menu {

    /**
     * Проверяет, вызывает ли сообщение данное меню
     */
    boolean matchSendMessage(Message message);

    /**
     * Отправляет меню
     */
    SendMessage getSendMessage(Message message);

    /**
     * Проверяет, это ли меню вызывается по нажатию кнопки
     */
    boolean matchEditMessage(CallbackQuery callbackQuery);

    /**
     * Превращает сообщение меню, в котором была нажата кнопка в данное меню
     */
    EditMessageText getEditMessage(CallbackQuery callbackQuery);
}
