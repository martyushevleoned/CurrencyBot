package ru.urfu.controller.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.controller.constant.MenuTypes;
import ru.urfu.controller.constant.UserCommands;
import ru.urfu.utils.callback.MultipageMenuCallback;

import java.util.List;

/**
 * Главное меню
 */
@Component
public class MainMenu implements CommandMenu, CallbackMenu {

    private final InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(InlineKeyboardButton.builder()
                    .text(MenuTypes.CURRENCY_ADD_TO_TRACK_LIST.getText())
                    .callbackData(new MultipageMenuCallback(MenuTypes.CURRENCY_ADD_TO_TRACK_LIST).getData())
                    .build()))
            .keyboardRow(List.of(InlineKeyboardButton.builder()
                    .text(MenuTypes.TRACKED_CURRENCY_LIST.getText())
                    .callbackData(new MultipageMenuCallback(MenuTypes.TRACKED_CURRENCY_LIST).getData())
                    .build()))
            .build();

    @Override
    public UserCommands getUserCommand() {
        return UserCommands.START;
    }

    @Override
    public SendMessage formSendMessage(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(MenuTypes.MAIN_MENU.getText())
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }

    @Override
    public MenuTypes getMenuType() {
        return MenuTypes.MAIN_MENU;
    }

    @Override
    public EditMessageText formEditMessage(CallbackQuery callbackQuery) {
        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(MenuTypes.MAIN_MENU.getText())
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }
}
