package ru.urfu.controller.menu.apiMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.ApiService;
import ru.urfu.controller.constant.ButtonsText;
import ru.urfu.controller.constant.MenuTypes;
import ru.urfu.controller.menu.CallbackMenu;
import ru.urfu.utils.callback.ApiMenuCallback;
import ru.urfu.utils.callback.MenuCallback;

import java.util.List;

/**
 * Меню с описанием конкретного API
 */
@Component
public class ApiMenu implements CallbackMenu {

    private final ApiService apiService;
    private final InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(InlineKeyboardButton.builder()
                    .text(ButtonsText.BACK.getText())
                    .callbackData(new MenuCallback(MenuTypes.API_LIST).getData())
                    .build()))
            .build();

    @Autowired
    public ApiMenu(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public MenuTypes getMenuType() {
        return MenuTypes.API;
    }

    @Override
    public EditMessageText formEditMessage(CallbackQuery callbackQuery) {
        String apiName = new ApiMenuCallback(callbackQuery).getApiName();
        String description = apiService.getDescription(apiName);

        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(description)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }
}
