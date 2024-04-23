package ru.urfu.controller.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.service.*;
import ru.urfu.utils.DataConverter;
import ru.urfu.utils.CallbackFlagManager;
import ru.urfu.utils.MultiPageKeyboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class TrackedCurrencyListMenu implements Menu {

    private final String text = "Отслеживаемые валюты";

    @Autowired
    private TrackService trackService;

    @Autowired
    private ApiService apiService;

    @Autowired
    private DataConverter dataConverter;

    @Autowired
    private CallbackFlagManager callbackFlagManager;

    @Autowired
    private MultiPageKeyboard multiPageKeyboard;

    private final String menuName = MenuType.TRACKED_CURRENCY_LIST.name();

    @Override
    public boolean matchSendMessage(Message message) {
        return message.getText().equals("/track");
    }

    @Override
    public SendMessage getSendMessage(Message message) {

        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(text)
                .replyMarkup(getInlineKeyboardMarkup(message.getChatId(), 0))
                .build();
    }

    @Override
    public boolean matchEditMessage(CallbackQuery callbackQuery) {
        return callbackQuery.getData().startsWith(menuName);
    }

    @Override
    public EditMessageText getEditMessage(CallbackQuery callback) {

        int pageIndex = Integer.parseInt(callbackFlagManager.getFlagOrDefault(callback.getData(), "0"));

        return EditMessageText.builder()
                .chatId(callback.getMessage().getChatId())
                .messageId(callback.getMessage().getMessageId())
                .text(text)
                .replyMarkup(getInlineKeyboardMarkup(callback.getMessage().getChatId(), pageIndex))
                .build();
    }

    /**
     * Генерирует клавиатуру для меню отслеживаемых валют.
     * Клавиатура состоит из кнопок - отслеживаемых валют + кнопка назад
     */
    private InlineKeyboardMarkup getInlineKeyboardMarkup(long chatId, int pageIndex) {
        List<InlineKeyboardButton> buttons = getTrackedButtons(chatId);

        List<List<InlineKeyboardButton>> rows = new ArrayList<>(multiPageKeyboard.getPage(pageIndex, buttons, menuName, 9));
        rows.add(List.of(InlineKeyboardButton.builder().text("Назад").callbackData(MenuType.MAIN_MENU.name()).build()));
        return new InlineKeyboardMarkup(rows);
    }

    private List<InlineKeyboardButton> getTrackedButtons(long chatId){
        return trackService.getTrackedRequests(chatId).stream()
                .map(r -> dataConverter.requestToInlineKeyboardButton(MenuType.TRACKED_CURRENCY.name(), r))
                .sorted(Comparator.comparing(InlineKeyboardButton::getText))
                .toList();
    }
}
