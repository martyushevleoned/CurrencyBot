package ru.urfu.controller.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.model.Request;
import ru.urfu.model.Response;
import ru.urfu.service.ApiService;
import ru.urfu.service.DataConverterService;
import ru.urfu.service.TextFormaterService;

import java.util.List;

@Component
public class TrackedCurrencyMenu implements Menu {

    @Autowired
    private ApiService apiService;

    @Autowired
    private TextFormaterService textFormaterService;

    @Autowired
    private DataConverterService dataConverter;

    private final String menuName = MenuType.TRACKED_CURRENCY.name();

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
        String callbackData = callbackQuery.getData().substring(menuName.length());
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = callbackQuery.getMessage().getMessageId();

        // запрос
        Request request = dataConverter.callbackDataToRequest(callbackData);
        Response response = apiService.getPrice(request);

        // текст меню
        String text = textFormaterService.getPriceInfo(request, response);

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .replyMarkup(getKeyboard(callbackQuery))
                .build();
    }

    /**
     * Генерирует клавиатуру для данного меню
     */
    private InlineKeyboardMarkup getKeyboard(CallbackQuery callbackQuery) {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder().text("Обновить курс").callbackData(callbackQuery.getData()).build()
                ))
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder().text("Назад").callbackData(MenuType.TRACKED_CURRENCY_LIST.name()).build()
                ))
                .build();
    }
}
