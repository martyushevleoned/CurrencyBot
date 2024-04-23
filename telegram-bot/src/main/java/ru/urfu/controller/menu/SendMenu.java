package ru.urfu.controller.menu;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Меню вызываемое при получении текстового сообщения
 */
public interface SendMenu extends Menu {

    /**
     * Вызывает ли текстовое сообщение данное меню
     */
    boolean matchSendMessage(Message message);

    /**
     * Формирует меню для отправки
     */
    SendMessage formSendMessage(Message message);
}