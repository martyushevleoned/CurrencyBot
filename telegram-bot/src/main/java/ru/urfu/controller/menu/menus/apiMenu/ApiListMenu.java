package ru.urfu.controller.menu.menus.apiMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.controller.menu.constant.UserCommands;
import ru.urfu.controller.menu.constant.Flags;
import ru.urfu.controller.menu.constant.MenuTypes;
import ru.urfu.controller.menu.CallbackMenu;
import ru.urfu.controller.menu.SendMenu;
import ru.urfu.service.ApiService;
import ru.urfu.utils.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Меню со списком всех поддерживаемых API
 */
@Component
public class ApiListMenu implements SendMenu, CallbackMenu {

    private static final String CURRENT_MENU_NAME = MenuTypes.API_LIST.getName();
    private static final String CURRENT_MENU_TEXT = MenuTypes.API_LIST.getText();
    private static final String API_COMMAND = UserCommands.API.getText();

    private static final String CHILD_MENU_NAME = MenuTypes.API.getName();
    private static final String API_NAME_FLAG = Flags.API_NAME_FLAG.getName();

    private final InlineKeyboardMarkup inlineKeyboardMarkup;

    /**
     * Инициализировать клавиатуру
     */
    @Autowired
    public ApiListMenu(ApiService apiService) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        apiService.getAllApiNames().stream().sorted().forEach(apiName -> {

            Callback callback = new Callback(CHILD_MENU_NAME);
            callback.addFlag(API_NAME_FLAG, apiName);

            rows.add(List.of(InlineKeyboardButton.builder()
                    .text(apiName)
                    .callbackData(callback.toString())
                    .build()));
        });
        inlineKeyboardMarkup = new InlineKeyboardMarkup(rows);
    }

    @Override
    public boolean matchSendMessage(Message message) {
        return message.getText().equals(API_COMMAND);
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
