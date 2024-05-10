package ru.urfu.controller.menu.apiMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.ApiService;
import ru.urfu.controller.constant.Menus;
import ru.urfu.controller.constant.UserCommands;
import ru.urfu.controller.menu.CallbackMenu;
import ru.urfu.controller.menu.CommandMenu;
import ru.urfu.utils.callback.ApiMenuCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Меню со списком всех поддерживаемых API
 */
@Component
public class ApiListMenu implements CommandMenu, CallbackMenu {

    private final InlineKeyboardMarkup inlineKeyboardMarkup;

    /**
     * Инициализировать клавиатуру
     */
    @Autowired
    public ApiListMenu(ApiService apiService) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        apiService.getAllApiNames().stream().sorted().forEach(apiName -> {

            ApiMenuCallback callback = new ApiMenuCallback(Menus.API, apiName);
            rows.add(List.of(InlineKeyboardButton.builder()
                    .text(apiName)
                    .callbackData(callback.getData())
                    .build()));
        });
        inlineKeyboardMarkup = new InlineKeyboardMarkup(rows);
    }

    @Override
    public String getCommand() {
        return UserCommands.API.getCommand();
    }

    @Override
    public SendMessage formSendMessage(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(Menus.API_LIST.getText())
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }

    @Override
    public Menus getMenu() {
        return Menus.API_LIST;
    }

    @Override
    public EditMessageText formEditMessage(CallbackQuery callbackQuery) {
        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(Menus.API_LIST.getText())
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }
}
