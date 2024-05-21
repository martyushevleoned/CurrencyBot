package ru.urfu.controller.menu.apiMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.ApiService;
import ru.urfu.controller.constant.ButtonText;
import ru.urfu.controller.constant.MenuType;
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
                    .text(ButtonText.BACK.getText())
                    .callbackData(new MenuCallback(MenuType.API_LIST).getData())
                    .build()))
            .build();

    @Autowired
    public ApiMenu(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.API;
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
