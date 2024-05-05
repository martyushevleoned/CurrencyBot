package ru.urfu.controller.menu.menus;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.controller.menu.constant.UserCommands;
import ru.urfu.controller.menu.constant.MenuTypes;
import ru.urfu.controller.menu.CallbackMenu;
import ru.urfu.controller.menu.SendMenu;
import ru.urfu.utils.Callback;

import java.util.List;

/**
 * Главное меню
 */
@Component
public class MainMenu implements SendMenu, CallbackMenu {

    private static final String CURRENT_MENU_NAME = MenuTypes.MAIN_MENU.getName();
    private static final String CURRENT_MENU_TEXT = MenuTypes.MAIN_MENU.getText();
    private static final String START_COMMAND = UserCommands.START.getText();

    private static final String ALL_CURRENCIES_MENU_NAME = MenuTypes.CURRENCY_ADD_TO_TRACK_LIST.getName();
    private static final String ALL_CURRENCIES_MENU_BUTTON_TEXT = MenuTypes.CURRENCY_ADD_TO_TRACK_LIST.getText();

    private static final String TRACKED_CURRENCIES_MENU_NAME = MenuTypes.TRACKED_CURRENCY_LIST.getName();
    private static final String TRACKED_CURRENCIES_MENU_BUTTON_TEXT = MenuTypes.TRACKED_CURRENCY_LIST.getText();

    private final InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(InlineKeyboardButton.builder()
                    .text(ALL_CURRENCIES_MENU_BUTTON_TEXT)
                    .callbackData(new Callback(ALL_CURRENCIES_MENU_NAME).toString())
                    .build()))
            .keyboardRow(List.of(InlineKeyboardButton.builder()
                    .text(TRACKED_CURRENCIES_MENU_BUTTON_TEXT)
                    .callbackData(new Callback(TRACKED_CURRENCIES_MENU_NAME).toString())
                    .build()))
            .build();

    @Override
    public boolean matchSendMessage(Message message) {
        return message.getText().equals(START_COMMAND);
    }

    @Override
    public SendMessage formSendMessage(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(CURRENT_MENU_TEXT)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }

    @Override
    public boolean matchEditMessage(CallbackQuery callbackQuery) {
        return callbackQuery.getData().equals(CURRENT_MENU_NAME);
    }

    @Override
    public EditMessageText formEditMessage(CallbackQuery callbackQuery) {
        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(CURRENT_MENU_TEXT)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }
}
