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
import ru.urfu.service.DataConverterService;
import ru.urfu.service.TrackService;

import java.util.ArrayList;
import java.util.List;

@Component
public class TrackedCurrencyListMenu implements Menu {

    private final String text = "Отслеживаемые валюты";

    @Autowired
    private TrackService trackService;

    @Autowired
    private ApiService apiService;

    @Autowired
    private DataConverterService dataConverter;

    @Override
    public boolean matchSendMessage(Message message) {
        return message.getText().equals("/track");
    }

    @Override
    public SendMessage getSendMessage(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(text)
                .replyMarkup(getInlineKeyboardMarkup(message.getChatId()))
                .build();
    }

    @Override
    public boolean matchEditMessage(CallbackQuery callbackQuery) {
        return callbackQuery.getData().equals(MenuType.TRACKED_CURRENCY_LIST.name());
    }

    @Override
    public EditMessageText getEditMessage(CallbackQuery callback) {
        return EditMessageText.builder()
                .chatId(callback.getMessage().getChatId())
                .messageId(callback.getMessage().getMessageId())
                .text(text)
                .replyMarkup(getInlineKeyboardMarkup(callback.getMessage().getChatId()))
                .build();
    }

    /**
     * Генерирует клавиатуру для меню отслеживаемых валют.
     * Клавиатура состоит из кнопок - отслеживаемых валют + кнопка назад
     */
    private InlineKeyboardMarkup getInlineKeyboardMarkup(long chatId) {

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> buttons = trackService.getTrackedRequests(chatId)
                .stream().limit(9)
                .map(dataConverter::requestToInlineKeyboardButton)
                .toList();

        buttons.forEach(b -> {
            b.setCallbackData(MenuType.TRACKED_CURRENCY.name() + b.getCallbackData());
            rows.add(List.of(b));
        });

        rows.add(List.of(
                InlineKeyboardButton.builder().text("Назад").callbackData(MenuType.MAIN_MENU.name()).build()
        ));

        return new InlineKeyboardMarkup(rows);
    }
}
