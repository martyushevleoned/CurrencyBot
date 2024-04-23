package ru.urfu.controller.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.model.CurrencyRequest;
import ru.urfu.model.CurrencyResponse;
import ru.urfu.service.*;
import ru.urfu.utils.DataConverter;
import ru.urfu.utils.CallbackFlagManager;
import ru.urfu.utils.TextFormater;

import java.util.List;

@Component
public class CurrencyMenu implements Menu {

    @Autowired
    private ApiService apiService;

    @Autowired
    private TrackService trackService;

    @Autowired
    private TextFormater textFormater;

    @Autowired
    private DataConverter dataConverter;

    @Autowired
    private CallbackFlagManager callbackFlagManager;

    private final String menuName = MenuType.CURRENCY_ADD_TO_TRACK.name();

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

        // параметры колбэка
        String callbackData = callbackQuery.getData().substring(menuName.length());
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = callbackQuery.getMessage().getMessageId();

        // обработка флага
        if (callbackFlagManager.containsFlag(callbackData)) {
            callbackData = callbackFlagManager.getBody(callbackData);
            trackService.addToTrack(chatId, dataConverter.callbackDataToRequest(callbackData));
        }

        // запрос
        CurrencyRequest currencyRequest = dataConverter.callbackDataToRequest(callbackData);
        CurrencyResponse currencyResponse = apiService.getPrice(currencyRequest);

        // текст меню
        String text = textFormater.getPriceInfo(currencyRequest, currencyResponse);

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .replyMarkup(getInlineKeyboardMarkup(chatId, callbackData))
                .build();
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup(long chatId, String callbackData) {

        String addToTrackCallback = callbackFlagManager.setFlag(menuName + callbackData, "");
        String addToTrackText;

        if (trackService.isTracked(chatId, dataConverter.callbackDataToRequest(callbackData))) {
            addToTrackText = "Удалить из отслеживаемых";
        } else {
            addToTrackText = "Добавить в отслеживаемые";
        }

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder().text(addToTrackText).callbackData(addToTrackCallback).build()
                ))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder().text("Назад").callbackData(MenuType.CURRENCY_ADD_TO_TRACK_LIST.name()).build()
                ))
                .build();
    }
}
