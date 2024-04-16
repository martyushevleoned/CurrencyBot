package ru.urfu.controller.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class MainMenu implements Menu {

    private final String text = "Главное меню";

    private final InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(
                    InlineKeyboardButton.builder().text("Все валюты").callbackData(MenuType.CURRENCY_ADD_TO_TRACK_LIST.name()).build()
            ))
            .keyboardRow(List.of(
                    InlineKeyboardButton.builder().text("Отслеживаемые валюты").callbackData(MenuType.TRACKED_CURRENCY_LIST.name()).build()
            ))
            .build();

    @Override
    public boolean matchSendMessage(Message message) {
        return message.getText().equals("/start");
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
        return callbackQuery.getData().equals(MenuType.MAIN_MENU.name());
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
