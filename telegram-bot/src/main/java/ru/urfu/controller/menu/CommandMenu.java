package ru.urfu.controller.menu;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.urfu.controller.constant.UserCommands;

/**
 * Меню вызываемое при получении текстовой команды
 */
public interface CommandMenu {

    /**
     * Получить текст команды по которой вызывается меню
     */
    UserCommands getUserCommand();

    /**
     * Формирует меню для отправки
     */
    SendMessage formSendMessage(Message message);
}