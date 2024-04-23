package ru.urfu.controller.menu.menus.currencyMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.controller.menu.constant.ButtonsText;
import ru.urfu.controller.menu.constant.UserCommands;
import ru.urfu.controller.menu.constant.Flags;
import ru.urfu.controller.menu.constant.MenuTypes;
import ru.urfu.controller.menu.CallbackMenu;
import ru.urfu.controller.menu.SendMenu;
import ru.urfu.service.ApiService;
import ru.urfu.utils.Callback;
import ru.urfu.utils.MultiPageKeyboard;
import ru.urfu.utils.TextFormater;

import java.util.*;

/**
 * Меню со списком всех поддерживаемых валют
 */
@Component
public class CurrencyListMenu implements SendMenu, CallbackMenu {

    private static final String CURRENT_MENU_NAME = MenuTypes.CURRENCY_ADD_TO_TRACK_LIST.getName();
    private static final String CURRENT_MENU_TEXT = MenuTypes.CURRENCY_ADD_TO_TRACK_LIST.getText();
    private static final String CURRENCIES_COMMAND = UserCommands.CURRENCIES.getText();

    private static final String PARENT_MENU_NAME = MenuTypes.MAIN_MENU.getName();
    private static final String CHILD_MENU_NAME = MenuTypes.CURRENCY_ADD_TO_TRACK.getName();

    private static final String BACK_BUTTON_TEXT = ButtonsText.BACK.getText();

    private static final String API_NAME_FLAG = Flags.API_NAME_FLAG.getName();
    private static final String CURRENCY_NAME_FLAG = Flags.CURRENCY_NAME_FLAG.getName();
    private static final String PAGE_INDEX_FLAG = Flags.PAGE_INDEX_FLAG.getName();

    private final TextFormater textFormater;
    private final List<InlineKeyboardMarkup> inlineKeyboardMarkups;

    @Autowired
    public CurrencyListMenu(ApiService apiService,
                            MultiPageKeyboard multiPageKeyboard,
                            TextFormater textFormater) {
        this.textFormater = textFormater;
        inlineKeyboardMarkups = initKeyboard(apiService, multiPageKeyboard);
    }

    /**
     * Инициализировать список клавиатур для многостраничного меню
     */
    private List<InlineKeyboardMarkup> initKeyboard(ApiService apiService,
                                                    MultiPageKeyboard multiPageKeyboard){
        List<InlineKeyboardButton> buttons = apiService.getPossibleRequests().stream()
                .map(currencyRequest -> {
                    Callback callback = new Callback(CHILD_MENU_NAME);
                    callback.addFlag(API_NAME_FLAG, currencyRequest.getApi());
                    callback.addFlag(CURRENCY_NAME_FLAG, currencyRequest.getCurrency());
                    return InlineKeyboardButton.builder()
                            .text(textFormater.getCurrencyInfo(currencyRequest))
                            .callbackData(callback.toString())
                            .build();
                })
                .sorted(Comparator.comparing(InlineKeyboardButton::getText))
                .toList();

        int keyboardMarkupsCount = multiPageKeyboard.getCountOfPages(buttons.size(), 9);
        List<InlineKeyboardMarkup> keyboardMarkups = new ArrayList<>();

        for (int i = 0; i < keyboardMarkupsCount; i++) {
            List<List<InlineKeyboardButton>> rows = new ArrayList<>(10);
            rows.addAll(multiPageKeyboard.getPage(i, buttons, CURRENT_MENU_NAME, 9));
            rows.add(List.of(InlineKeyboardButton.builder().text(BACK_BUTTON_TEXT).callbackData(PARENT_MENU_NAME).build()));
            keyboardMarkups.add(new InlineKeyboardMarkup(rows));
        }

         return Collections.unmodifiableList(keyboardMarkups);
    }

    @Override
    public boolean matchSendMessage(Message message) {
        return message.getText().equals(CURRENCIES_COMMAND);
    }

    @Override
    public SendMessage formSendMessage(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(CURRENT_MENU_TEXT)
                .replyMarkup(inlineKeyboardMarkups.get(0))
                .build();
    }

    @Override
    public boolean matchEditMessage(CallbackQuery callbackQuery) {
        return callbackQuery.getData().startsWith(CURRENT_MENU_NAME);
    }

    @Override
    public EditMessageText formEditMessage(CallbackQuery callbackQuery) {

        int pageIndex = 0;
        Callback callback = new Callback(callbackQuery);
        if (callback.containsFlag(PAGE_INDEX_FLAG))
            pageIndex = Integer.parseInt(Objects.requireNonNull(callback.getFlag(PAGE_INDEX_FLAG)));

        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(CURRENT_MENU_TEXT)
                .replyMarkup(inlineKeyboardMarkups.get(pageIndex))
                .build();
    }
}
