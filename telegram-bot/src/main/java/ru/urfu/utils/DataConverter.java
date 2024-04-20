package ru.urfu.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.model.CurrencyRequest;

/**
 * Преобразует CallbackData в Request и ,Request в InlineKeyboardButton.
 * Экземпляры этих классов одннозначно преобразуются друг в друга.
 */
@Component
public class DataConverter {

    private final String splitSequence = "#";

    /**
     * Преобразует callback (от нажатия кнопки) в Request
     */
    public CurrencyRequest callbackDataToRequest(String callbackData) {
        String[] parts = callbackData.split(splitSequence);
        return new CurrencyRequest(parts[1], parts[0]);
    }

    /**
     * Создаёт кнопку по параметрам Request.
     * @param menuName название меню в котором будет открываться валюта
     */
    public InlineKeyboardButton requestToInlineKeyboardButton(String menuName, CurrencyRequest currencyRequest) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(currencyRequest.getCurrency() + " - " + currencyRequest.getApi());
        button.setCallbackData(menuName + currencyRequest.getCurrency() + splitSequence + currencyRequest.getApi());
        return button;
    }
}
