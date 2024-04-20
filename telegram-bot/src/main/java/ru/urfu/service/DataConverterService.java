package ru.urfu.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.model.CurrencyRequest;

/**
 * Преобразует CallbackData в Request и ,Request в InlineKeyboardButton.
 * Экземпляры этих классов одннозначно преобразуются друг в друга.
 */
@Service
public class DataConverterService {

    private final String splitSequence = "#";

    /**
     * Преобразует callback (от нажатия кнопки) в Request
     */
    public CurrencyRequest callbackDataToRequest(String callbackData) {
        String[] parts = callbackData.split(splitSequence);
        return new CurrencyRequest(parts[1], parts[0]);
    }

    /**
     * Создаёт кнопку по параметрам Request
     */
    public InlineKeyboardButton requestToInlineKeyboardButton(CurrencyRequest currencyRequest) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(currencyRequest.getCurrency() + " - " + currencyRequest.getApi());
        button.setCallbackData(currencyRequest.getCurrency() + splitSequence + currencyRequest.getApi());
        return button;
    }
}
