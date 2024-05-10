package ru.urfu.controller.menu.currencyMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.ApiService;
import ru.urfu.controller.constant.ButtonsText;
import ru.urfu.controller.constant.Menus;
import ru.urfu.controller.menu.CallbackMenu;
import ru.urfu.model.CurrencyRequest;
import ru.urfu.model.CurrencyResponse;
import ru.urfu.service.TrackService;
import ru.urfu.utils.TextFormater;
import ru.urfu.utils.callback.CurrencyRequestTrackMenuCallback;
import ru.urfu.utils.callback.MultipageMenuCallback;

import java.util.List;

/**
 * Меню конкретной валюты с кнопкой для добавления в "отслеживаемые"
 */
@Component
public class CurrencyMenu implements CallbackMenu {

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
    public Menus getMenu() {
        return Menus.CURRENCY_ADD_TO_TRACK;
    }

    @Override
    public EditMessageText formEditMessage(CallbackQuery callbackQuery) {

        // параметры колбэка
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = callbackQuery.getMessage().getMessageId();

        // запрос
        CurrencyRequestTrackMenuCallback callback = new CurrencyRequestTrackMenuCallback(callbackQuery);
        CurrencyRequest currencyRequest = callback.getCurrencyRequest();
        CurrencyResponse currencyResponse = apiService.getPrice(currencyRequest);

        // обработка опций
        if (callback.isAddToTrack()) {
            trackService.addToTrack(chatId, currencyRequest);
        } else if (callback.isRemoveFromTrack()) {
            trackService.removeFromTrack(chatId, currencyRequest);
        }

        // текст меню
        String text = textFormater.getPriceInfo(currencyRequest, currencyResponse);

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .replyMarkup(getInlineKeyboardMarkup(chatId, currencyRequest))
                .build();
    }

    /**
     * Сформировать клавиатуру для меню добавления валюты в "отслеживаемые"
     *
     * @param chatId          идентификатор пользователя
     * @param currencyRequest валюта для добавления/удаления из отслеживаемых
     */
    private InlineKeyboardMarkup getInlineKeyboardMarkup(long chatId, CurrencyRequest currencyRequest) {

        String addToTrackText;
        CurrencyRequestTrackMenuCallback callback = new CurrencyRequestTrackMenuCallback(Menus.CURRENCY_ADD_TO_TRACK, currencyRequest);

        if (trackService.isTracked(chatId, currencyRequest)) {
            addToTrackText = ButtonsText.REMOVE_FROM_TRACK.getText();
            callback.addRemoveFromTrackFlag();
        } else {
            addToTrackText = ButtonsText.ADD_TO_TRACK.getText();
            callback.addAddToTrackFlag();
        }

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text(addToTrackText)
                        .callbackData(callback.getData())
                        .build()))
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text(ButtonsText.BACK.getText())
                        .callbackData(new MultipageMenuCallback(Menus.CURRENCY_ADD_TO_TRACK_LIST).getData())
                        .build()))
                .build();
    }
}
