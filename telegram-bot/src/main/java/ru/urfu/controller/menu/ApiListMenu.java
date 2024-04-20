package ru.urfu.controller.menu;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.service.ApiService;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApiListMenu implements Menu {

    private final String text = "Все API";

    private InlineKeyboardMarkup inlineKeyboardMarkup;

    @Autowired
    private ApiService apiService;

    @PostConstruct
    private void initKeyboard() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        apiService.getAllApiNames().forEach(apiName ->
                rows.add(List.of(
                        InlineKeyboardButton.builder()
                                .text(apiName)
                                .callbackData(MenuType.API.name() + apiName)
                                .build()
                ))
        );
        inlineKeyboardMarkup = new InlineKeyboardMarkup(rows);
    }

    @Override
    public boolean matchSendMessage(Message message) {
        return message.getText().equals("/api");
    }

    @Override
    public SendMessage getSendMessage(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(text)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }

    @Override
    public boolean matchEditMessage(CallbackQuery callbackQuery) {
        return callbackQuery.getData().equals(MenuType.API_LIST.name());
    }

    @Override
    public EditMessageText getEditMessage(CallbackQuery callbackQuery) {
        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(text)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }
}
