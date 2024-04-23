package ru.urfu.controller.menu;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.service.ApiService;
import ru.urfu.utils.DataConverter;
import ru.urfu.utils.CallbackFlagManager;
import ru.urfu.utils.MultiPageKeyboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class CurrencyListMenu implements Menu {

    private final String text = "Все валюты";

    @Autowired
    private ApiService apiService;

    @Autowired
    private DataConverter dataConverter;

    @Autowired
    private CallbackFlagManager callbackFlagManager;

    @Autowired
    private MultiPageKeyboard multiPageKeyboard;

    private List<InlineKeyboardButton> buttons;

    private final String menuName = MenuType.CURRENCY_ADD_TO_TRACK_LIST.name();

    /**
     * Инициализирует кнопки в меню всех валют
     */
    @PostConstruct
    private void initButtons() {
        buttons = apiService.getPossibleRequests().stream()
                .map(r -> dataConverter.requestToInlineKeyboardButton(MenuType.CURRENCY_ADD_TO_TRACK.name(), r))
                .sorted(Comparator.comparing(InlineKeyboardButton::getText))
                .toList();
    }

    @Override
    public boolean matchSendMessage(Message message) {
        return message.getText().equals("/currencies");
    }

    @Override
    public SendMessage getSendMessage(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(text)
                .replyMarkup(getKeyboard(0))
                .build();
    }

    @Override
    public boolean matchEditMessage(CallbackQuery callbackQuery) {
        return callbackQuery.getData().startsWith(MenuType.CURRENCY_ADD_TO_TRACK_LIST.name());
    }

    @Override
    public EditMessageText getEditMessage(CallbackQuery callback) {

        int pageIndex = Integer.parseInt(callbackFlagManager.getFlagOrDefault(callback.getData(), "0"));

        return EditMessageText.builder()
                .chatId(callback.getMessage().getChatId())
                .messageId(callback.getMessage().getMessageId())
                .text(text)
                .replyMarkup(getKeyboard(pageIndex))
                .build();
    }

    /**
     * Собирает кнопки указанной страницы меню
     */
    private InlineKeyboardMarkup getKeyboard(int pageIndex) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.addAll(multiPageKeyboard.getPage(pageIndex, buttons, menuName, 9));
        rows.add(List.of(InlineKeyboardButton.builder().text("Назад").callbackData(MenuType.MAIN_MENU.name()).build()));
        return new InlineKeyboardMarkup(rows);
    }
}
