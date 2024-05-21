package ru.urfu.controller.menu;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.urfu.controller.constant.UserCommand;

/**
 * Меню вызываемое при получении текстовой команды
 */
public interface CommandMenu {

    /**
     * Получить текст команды по которой вызывается меню
     */
    UserCommand getUserCommand();

    /**
     * Формирует меню для отправки
     */
    SendMessage formSendMessage(long chatId);
}