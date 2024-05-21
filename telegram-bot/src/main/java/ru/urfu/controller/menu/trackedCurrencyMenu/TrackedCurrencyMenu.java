package ru.urfu.controller.menu.trackedCurrencyMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.ApiService;
import ru.urfu.controller.constant.ButtonText;
import ru.urfu.controller.constant.MenuType;
import ru.urfu.controller.menu.CallbackMenu;
import ru.urfu.model.CurrencyRequest;
import ru.urfu.model.CurrencyResponse;
import ru.urfu.utils.callback.*;
import ru.urfu.utils.TextFormater;

import java.util.List;

/**
 * Меню конкретной отслеживаемой валюты
 */
@Component
public class TrackedCurrencyMenu implements CallbackMenu {

    private final ApiService apiService;
    private final TextFormater textFormater;

    @Autowired
    public TrackedCurrencyMenu(ApiService apiService, TextFormater textFormater) {
        this.apiService = apiService;
        this.textFormater = textFormater;
    }

    @Override
    public MenuType getMenuType() {
        return  MenuType.TRACKED_CURRENCY;
    }

    @Override
    public EditMessageText formEditMessage(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = callbackQuery.getMessage().getMessageId();

        // запрос
        CurrencyRequestFlagMenuCallback callback = new CurrencyRequestFlagMenuCallback(callbackQuery);
        CurrencyRequest currencyRequest = callback.getCurrencyRequest();
        CurrencyResponse currencyResponse = apiService.getPrice(currencyRequest);

        // текст меню
        String text = textFormater.getPriceInfo(currencyRequest, currencyResponse);

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .replyMarkup(getKeyboard(callback))
                .build();
    }

    /**
     * Сгенерировать клавиатуру для данного меню
     */
    private InlineKeyboardMarkup getKeyboard(CurrencyRequestFlagMenuCallback callback) {

        // API телеграмма вызывает исключение если заменить меню на точно такое же меню,
        // Чтобы отправлять не идентичное меню в колбэк кнопки "Обновить курс" добавляется флаг
        // если флаг в колбэке уже есть то он убирается
        callback.invertFlag();

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text(ButtonText.UPDATE_PRICE.getText())
                        .callbackData(callback.getData()).build()))
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text(ButtonText.BACK.getText())
                        .callbackData(new MultipageMenuCallback(MenuType.TRACKED_CURRENCY_LIST).getData()).build()))
                .build();
    }
}
