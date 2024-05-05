package ru.urfu.controller.menu.menus.apiMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.controller.menu.constant.ButtonsText;
import ru.urfu.controller.menu.constant.Flags;
import ru.urfu.controller.menu.constant.MenuTypes;
import ru.urfu.controller.menu.CallbackMenu;
import ru.urfu.service.ApiService;
import ru.urfu.utils.Callback;

import java.util.List;

/**
 * Меню с описанием конкретного API
 */
@Component
public class ApiMenu implements CallbackMenu {

    private final static String CURRENT_MENU_NAME = MenuTypes.API.getName();
    private final static String CHILD_MENU_NAME = MenuTypes.API_LIST.getName();
    private final static String BACK_BUTTON_TEXT = ButtonsText.BACK.getText();
    private final static String API_NAME_FLAG = Flags.API_NAME_FLAG.getName();

    private final ApiService apiService;
    private final InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(InlineKeyboardButton.builder()
                    .text(BACK_BUTTON_TEXT)
                    .callbackData(CHILD_MENU_NAME)
                    .build()))
            .build();

    @Autowired
    public ApiMenu(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public boolean matchEditMessage(CallbackQuery callbackQuery) {
        return callbackQuery.getData().startsWith(CURRENT_MENU_NAME);
    }

    @Override
    public EditMessageText formEditMessage(CallbackQuery callbackQuery) {
        Callback callback = new Callback(callbackQuery);
        String apiName = callback.getFlag(API_NAME_FLAG);
        String description = apiService.getDescription(apiName);

        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(description)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }
}
