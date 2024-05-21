package ru.urfu.controller.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.controller.constant.MenuType;
import ru.urfu.controller.constant.UserCommand;
import ru.urfu.utils.callback.MultipageMenuCallback;

import java.util.List;

/**
 * Главное меню
 */
@Component
public class MainMenu implements CommandMenu, CallbackMenu {

    private final InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(InlineKeyboardButton.builder()
                    .text(MenuType.CURRENCY_ADD_TO_TRACK_LIST.getText())
                    .callbackData(new MultipageMenuCallback(MenuType.CURRENCY_ADD_TO_TRACK_LIST).getData())
                    .build()))
            .keyboardRow(List.of(InlineKeyboardButton.builder()
                    .text(MenuType.TRACKED_CURRENCY_LIST.getText())
                    .callbackData(new MultipageMenuCallback(MenuType.TRACKED_CURRENCY_LIST).getData())
                    .build()))
            .build();

    @Override
    public UserCommand getUserCommand() {
        return UserCommand.START;
    }

    @Override
    public SendMessage formSendMessage(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(MenuType.MAIN_MENU.getText())
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.MAIN_MENU;
    }

    @Override
    public EditMessageText formEditMessage(CallbackQuery callbackQuery) {
        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(MenuType.MAIN_MENU.getText())
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }
}
