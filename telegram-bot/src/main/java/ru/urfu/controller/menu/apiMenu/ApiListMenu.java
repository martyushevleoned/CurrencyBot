package ru.urfu.controller.menu.apiMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.ApiService;
import ru.urfu.controller.constant.MenuType;
import ru.urfu.controller.constant.UserCommand;
import ru.urfu.controller.menu.CallbackMenu;
import ru.urfu.controller.menu.CommandMenu;
import ru.urfu.utils.callback.ApiMenuCallback;
import ru.urfu.utils.callback.MenuCallback;

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
        apiService.getAllApiNames().stream()
                .sorted()
                .forEach(apiName -> {

            ApiMenuCallback callback = new ApiMenuCallback(MenuType.API, apiName);
            rows.add(List.of(InlineKeyboardButton.builder()
                    .text(apiName)
                    .callbackData(callback.getData())
                    .build()));
        });
        inlineKeyboardMarkup = new InlineKeyboardMarkup(rows);
    }

    @Override
    public UserCommand getUserCommand() {
        return UserCommand.API;
    }

    @Override
    public SendMessage formSendMessage(long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MenuType.API_LIST.getText())
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.API_LIST;
    }

    @Override
    public EditMessageText formEditMessage(long chatId, int messageId, MenuCallback menuCallback) {
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(MenuType.API_LIST.getText())
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }
}
