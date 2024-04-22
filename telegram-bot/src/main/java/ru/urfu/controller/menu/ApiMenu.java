package ru.urfu.controller.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.service.ApiService;

import java.util.List;

@Component
public class ApiMenu implements Menu {

    @Autowired
    private ApiService apiService;

    private final String menuName = MenuType.API.name();

    private final InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(
                    InlineKeyboardButton.builder().text("Назад").callbackData(MenuType.API_LIST.name()).build()
            ))
            .build();

    @Override
    public boolean matchSendMessage(Message message) {
        return false;
    }

    @Override
    public SendMessage getSendMessage(Message message) {
        throw new RuntimeException("get Message is unreachable");
    }

    @Override
    public boolean matchEditMessage(CallbackQuery callbackQuery) {
        return callbackQuery.getData().startsWith(menuName);
    }

    @Override
    public EditMessageText getEditMessage(CallbackQuery callbackQuery) {
        String apiName = callbackQuery.getData().substring(menuName.length());
        String text = "API: " + apiName + "\n" + apiService.getDescription(apiName);

        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(text)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }
}
