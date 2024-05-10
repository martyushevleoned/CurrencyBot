package ru.urfu.controller.menu;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Меню вызываемое при получении текстовой команды
 */
public interface CommandMenu {

    /**
     * Получить текст команды по которой вызывается меню
     */
    String getCommand();

    /**
     * Формирует меню для отправки
     */
    SendMessage formSendMessage(Message message);
}