package ru.urfu.controller.menu.menus.currencyMenu;

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
import ru.urfu.model.CurrencyRequest;
import ru.urfu.model.CurrencyResponse;
import ru.urfu.service.ApiService;
import ru.urfu.service.TrackService;
import ru.urfu.utils.Callback;
import ru.urfu.utils.TextFormater;

import java.util.List;

/**
 * Меню конкретной валюты с кнопокой для добавления в "отслеживаемые"
 */
@Component
public class CurrencyMenu implements CallbackMenu {

    private static final String CURRENT_MENU_NAME = MenuTypes.CURRENCY_ADD_TO_TRACK.getName();
    private static final String PARENT_MENU_NAME = MenuTypes.CURRENCY_ADD_TO_TRACK_LIST.getName();

    private static final String ADD_TO_TRACK_BUTTON_TEXT = ButtonsText.ADD_TO_TRACK.getText();
    private static final String REMOVE_FROM_BUTTON_TEXT = ButtonsText.REMOVE_FROM_TRACK.getText();
    private static final String BACK_BUTTON_TEXT = ButtonsText.BACK.getText();

    private static final String API_NAME_FLAG = Flags.API_NAME_FLAG.getName();
    private static final String CURRENCY_NAME_FLAG = Flags.CURRENCY_NAME_FLAG.getName();
    private static final String ADD_FLAG = Flags.ADD_TO_TRACK_FLAG.getName();
    private static final String REMOVE_FLAG = Flags.REMOVE_FROM_TRACK_FLAG.getName();

    private final ApiService apiService;
    private final TrackService trackService;
    private final TextFormater textFormater;

    @Autowired
    public CurrencyMenu(ApiService apiService,
                        TrackService trackService,
                        TextFormater textFormater) {
        this.apiService = apiService;
        this.trackService = trackService;
        this.textFormater = textFormater;
    }

    @Override
    public boolean matchEditMessage(CallbackQuery callbackQuery) {
        return callbackQuery.getData().startsWith(CURRENT_MENU_NAME);
    }

    @Override
    public EditMessageText formEditMessage(CallbackQuery callbackQuery) {

        // параметры колбэка
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = callbackQuery.getMessage().getMessageId();

        // запрос
        Callback callback = new Callback(callbackQuery);
        CurrencyRequest currencyRequest = new CurrencyRequest(
                callback.getFlag(API_NAME_FLAG),
                callback.getFlag(CURRENCY_NAME_FLAG)
        );
        CurrencyResponse currencyResponse = apiService.getPrice(currencyRequest);

        // обработка флага
        if (callback.containsFlag(ADD_FLAG)) {
            callback.removeFlag(ADD_FLAG);
            trackService.addToTrack(chatId, currencyRequest);
        } else if (callback.containsFlag(REMOVE_FLAG)) {
            callback.removeFlag(REMOVE_FLAG);
            trackService.removeFromTrack(chatId, currencyRequest);
        }

        // текст меню
        String text = textFormater.getPriceInfo(currencyRequest, currencyResponse);

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .replyMarkup(getInlineKeyboardMarkup(chatId, callback, currencyRequest))
                .build();
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup(long chatId, Callback callback, CurrencyRequest currencyRequest) {

        String addToTrackText;
        if (trackService.isTracked(chatId, currencyRequest)) {
            addToTrackText = REMOVE_FROM_BUTTON_TEXT;
            callback.addFlag(REMOVE_FLAG, "");

        } else {
            callback.addFlag(ADD_FLAG, "");
            addToTrackText = ADD_TO_TRACK_BUTTON_TEXT;
        }

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text(addToTrackText)
                        .callbackData(callback.toString())
                        .build()))
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text(BACK_BUTTON_TEXT)
                        .callbackData(new Callback(PARENT_MENU_NAME).toString())
                        .build()))
                .build();
    }
}
