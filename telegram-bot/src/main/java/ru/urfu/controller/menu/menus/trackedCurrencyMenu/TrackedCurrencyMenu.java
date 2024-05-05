package ru.urfu.controller.menu.menus.trackedCurrencyMenu;

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
import ru.urfu.utils.Callback;
import ru.urfu.utils.TextFormater;

import java.util.List;

/**
 * Меню конкретной отслеживаемой валюты
 */
@Component
public class TrackedCurrencyMenu implements CallbackMenu {

    private static final String CURRENT_MENU_NAME = MenuTypes.TRACKED_CURRENCY.getName();
    private static final String PARENT_MENU_NAME = MenuTypes.TRACKED_CURRENCY_LIST.getName();

    private static final String BACK_BUTTON_TEXT = ButtonsText.BACK.getText();
    private static final String UPDATE_PRICE_BUTTON_TEXT = ButtonsText.UPDATE_PRICE.getText();

    private static final String API_NAME_FLAG = Flags.API_NAME_FLAG.getName();
    private static final String CURRENCY_NAME_FLAG = Flags.CURRENCY_NAME_FLAG.getName();
    private static final String CONTAINS_FLAG = Flags.CONTAINS.getName();

    private final ApiService apiService;
    private final TextFormater textFormater;

    @Autowired
    public TrackedCurrencyMenu(ApiService apiService, TextFormater textFormater) {
        this.apiService = apiService;
        this.textFormater = textFormater;
    }

    @Override
    public boolean matchEditMessage(CallbackQuery callbackQuery) {
        return callbackQuery.getData().startsWith(CURRENT_MENU_NAME);
    }

    @Override
    public EditMessageText formEditMessage(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = callbackQuery.getMessage().getMessageId();

        // запрос
        Callback callback = new Callback(callbackQuery);
        CurrencyRequest currencyRequest = new CurrencyRequest(
                callback.getFlag(API_NAME_FLAG),
                callback.getFlag(CURRENCY_NAME_FLAG)
        );
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
    private InlineKeyboardMarkup getKeyboard(Callback callback) {

        // API телеграмма вызывает исключение если заменить меню на точно такое же меню
        // Чтобы отправлять не идентичное меню в колбэк кнопки "Обновить курс" добавляется флаг
        // если флаг в колбэке уже есть то он убирается

        if (callback.containsFlag(CONTAINS_FLAG))
            callback.removeFlag(CONTAINS_FLAG);
        else
            callback.addFlag(CONTAINS_FLAG, "");

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder().text(UPDATE_PRICE_BUTTON_TEXT).callbackData(callback.toString()).build()))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder().text(BACK_BUTTON_TEXT).callbackData(new Callback(PARENT_MENU_NAME).toString()).build()))
                .build();
    }
}
