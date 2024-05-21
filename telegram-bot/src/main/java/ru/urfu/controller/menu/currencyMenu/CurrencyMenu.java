package ru.urfu.controller.menu.currencyMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.ApiService;
import ru.urfu.controller.constant.ButtonText;
import ru.urfu.controller.constant.MenuType;
import ru.urfu.controller.menu.CallbackMenu;
import ru.urfu.model.CurrencyRequest;
import ru.urfu.service.TrackService;
import ru.urfu.utils.TextFormater;
import ru.urfu.utils.callback.CurrencyRequestTrackMenuCallback;
import ru.urfu.utils.callback.MenuCallback;
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
    public MenuType getMenuType() {
        return MenuType.CURRENCY_ADD_TO_TRACK;
    }

    @Override
    public EditMessageText formEditMessage(long chatId, int messageId, MenuCallback menuCallback) {

        CurrencyRequestTrackMenuCallback callback = new CurrencyRequestTrackMenuCallback(menuCallback);
        CurrencyRequest currencyRequest = callback.getCurrencyRequest();

        // обработка опций
        if (callback.isAddToTrack()) {
            trackService.addToTrack(chatId, currencyRequest);
        } else if (callback.isRemoveFromTrack()) {
            trackService.removeFromTrack(chatId, currencyRequest);
        }

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(textFormater.getPriceInfo(currencyRequest, apiService.getPrice(currencyRequest))) //TODO добавить обработку ошибок
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
        CurrencyRequestTrackMenuCallback callback = new CurrencyRequestTrackMenuCallback(MenuType.CURRENCY_ADD_TO_TRACK, currencyRequest);

        if (trackService.isTracked(chatId, currencyRequest)) {
            addToTrackText = ButtonText.REMOVE_FROM_TRACK.getText();
            callback.addRemoveFromTrackFlag();
        } else {
            addToTrackText = ButtonText.ADD_TO_TRACK.getText();
            callback.addAddToTrackFlag();
        }

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text(addToTrackText)
                        .callbackData(callback.getData())
                        .build()))
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text(ButtonText.BACK.getText())
                        .callbackData(new MultipageMenuCallback(MenuType.CURRENCY_ADD_TO_TRACK_LIST).getData())
                        .build()))
                .build();
    }
}
