package ru.urfu.controller.menu.currencyMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.ApiService;
import ru.urfu.controller.constant.ButtonsText;
import ru.urfu.controller.constant.MenuTypes;
import ru.urfu.controller.constant.TelegramConstants;
import ru.urfu.controller.constant.UserCommands;
import ru.urfu.controller.menu.CallbackMenu;
import ru.urfu.controller.menu.CommandMenu;
import ru.urfu.utils.MultiPageKeyboard;
import ru.urfu.utils.TextFormater;
import ru.urfu.utils.callback.MenuCallback;
import ru.urfu.utils.callback.CurrencyRequestMenuCallback;
import ru.urfu.utils.callback.MultipageMenuCallback;

import java.util.*;

/**
 * Меню со списком всех поддерживаемых валют
 */
@Component
public class CurrencyListMenu implements CommandMenu, CallbackMenu {

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
    private List<InlineKeyboardMarkup> initKeyboard(ApiService apiService, MultiPageKeyboard multiPageKeyboard) {
        List<InlineKeyboardButton> buttons = apiService.getPossibleRequests().stream()
                .map(currencyRequest -> {
                    CurrencyRequestMenuCallback callback = new CurrencyRequestMenuCallback(MenuTypes.CURRENCY_ADD_TO_TRACK, currencyRequest);
                    return InlineKeyboardButton.builder()
                            .text(textFormater.getCurrencyInfo(currencyRequest))
                            .callbackData(callback.getData())
                            .build();
                })
                .sorted(Comparator.comparing(InlineKeyboardButton::getText))
                .toList();

        // -1 это запас для кнопки "назад"
        int keyboardMarkupsCount = multiPageKeyboard.getCountOfPages(buttons.size(), TelegramConstants.MAX_COUNT_OF_ROWS - 1);
        List<InlineKeyboardMarkup> keyboardMarkups = new ArrayList<>();

        for (int i = 0; i < keyboardMarkupsCount; i++) {
            List<List<InlineKeyboardButton>> rows = new ArrayList<>(
                    multiPageKeyboard.getPage(i, buttons, MenuTypes.CURRENCY_ADD_TO_TRACK_LIST, TelegramConstants.MAX_COUNT_OF_ROWS - 1)
            );
            rows.add(List.of(InlineKeyboardButton.builder()
                    .text(ButtonsText.BACK.getText())
                    .callbackData(new MenuCallback(MenuTypes.MAIN_MENU).getData())
                    .build()));
            keyboardMarkups.add(new InlineKeyboardMarkup(rows));
        }

        return Collections.unmodifiableList(keyboardMarkups);
    }

    @Override
    public UserCommands getUserCommand() {
        return UserCommands.CURRENCIES;
    }

    @Override
    public SendMessage formSendMessage(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(MenuTypes.CURRENCY_ADD_TO_TRACK_LIST.getText())
                .replyMarkup(inlineKeyboardMarkups.get(0))
                .build();
    }

    @Override
    public MenuTypes getMenuType() {
        return MenuTypes.CURRENCY_ADD_TO_TRACK_LIST;
    }

    @Override
    public EditMessageText formEditMessage(CallbackQuery callbackQuery) {
        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(MenuTypes.CURRENCY_ADD_TO_TRACK_LIST.getText())
                .replyMarkup(inlineKeyboardMarkups.get(new MultipageMenuCallback(callbackQuery).getPageIndex()))
                .build();
    }
}