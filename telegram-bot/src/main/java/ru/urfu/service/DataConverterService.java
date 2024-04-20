package ru.urfu.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.model.Request;

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
    public Request callbackDataToRequest(String callbackData) {
        String[] parts = callbackData.split(splitSequence);
        return new Request(parts[1], parts[0]);
    }

    /**
     * Создаёт кнопку по параметрам Request
     */
    public InlineKeyboardButton requestToInlineKeyboardButton(Request request) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(request.getCurrency() + " - " + request.getApi());
        button.setCallbackData(request.getCurrency() + splitSequence + request.getApi());
        return button;
    }
}
