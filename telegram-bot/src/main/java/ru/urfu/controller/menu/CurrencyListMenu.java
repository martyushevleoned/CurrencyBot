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
public class CurrencyListMenu implements Menu {

    private final String text = "Все валюты";

    @Autowired
    private ApiService apiService;

    private InlineKeyboardMarkup inlineKeyboardMarkup;

    /**
     * Инициализирует кнопки в меню всех валют.
     * Добавляет в начало CallbackData кнопок флаг того,
     * чтобы нажатие валюты обработало следующее меню
     */
    @PostConstruct
    private void initKeyboard() {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        apiService.getRequestButtons().stream().limit(9).forEach(b -> {
            b.setCallbackData(MenuType.CURRENCY_ADD_TO_TRACK + b.getCallbackData());
            rows.add(List.of(b));
        });
        rows.add(List.of(InlineKeyboardButton.builder().text("Назад").callbackData(MenuType.MAIN_MENU.name()).build()));
        inlineKeyboardMarkup = new InlineKeyboardMarkup(rows);
    }

    @Override
    public boolean matchSendMessage(Message message) {
        return message.getText().equals("/currencies");
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
        return callbackQuery.getData().equals(MenuType.CURRENCY_ADD_TO_TRACK_LIST.name());
    }

    @Override
    public EditMessageText getEditMessage(CallbackQuery callback) {
        return EditMessageText.builder()
                .chatId(callback.getMessage().getChatId())
                .messageId(callback.getMessage().getMessageId())
                .text(text)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
    }
}
