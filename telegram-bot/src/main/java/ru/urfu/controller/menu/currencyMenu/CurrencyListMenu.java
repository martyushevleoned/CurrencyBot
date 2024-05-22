package ru.urfu.controller.menu.currencyMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.ApiService;
import ru.urfu.controller.constant.ButtonText;
import ru.urfu.controller.constant.MenuType;
import ru.urfu.controller.constant.TelegramConstant;
import ru.urfu.controller.constant.UserCommand;
import ru.urfu.controller.menu.CallbackMenu;
import ru.urfu.controller.menu.CommandMenu;
import ru.urfu.utils.MultiPageKeyboard;
import ru.urfu.utils.TextFormater;
import ru.urfu.utils.callback.menuCallback.MenuCallback;
import ru.urfu.utils.callback.menuCallback.currecncyRequestMenuCallback.CurrencyRequestMenuCallback;
import ru.urfu.utils.callback.menuCallback.MultipageMenuCallback;

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
                .map(currencyRequest ->
                        InlineKeyboardButton.builder()
                                .text(textFormater.getCurrencyInfo(currencyRequest))
                                .callbackData(new CurrencyRequestMenuCallback(MenuType.CURRENCY_ADD_TO_TRACK, currencyRequest).getData())
                                .build()
                )
                .sorted(Comparator.comparing(InlineKeyboardButton::getText))
                .toList();

        // -1 это запас для кнопки "назад"
        int keyboardMarkupsCount = multiPageKeyboard.getCountOfPages(buttons.size(), TelegramConstant.MAX_COUNT_OF_ROWS - 1);
        List<InlineKeyboardMarkup> keyboardMarkups = new ArrayList<>();

        for (int i = 0; i < keyboardMarkupsCount; i++) {
            List<List<InlineKeyboardButton>> rows = new ArrayList<>(
                    multiPageKeyboard.getPage(i, buttons, MenuType.CURRENCY_ADD_TO_TRACK_LIST, TelegramConstant.MAX_COUNT_OF_ROWS - 1)
            );
            rows.add(List.of(InlineKeyboardButton.builder()
                    .text(ButtonText.BACK.getText())
                    .callbackData(new MenuCallback(MenuType.MAIN_MENU).getData())
                    .build()));
            keyboardMarkups.add(new InlineKeyboardMarkup(rows));
        }

        return Collections.unmodifiableList(keyboardMarkups);
    }

    @Override
    public UserCommand getUserCommand() {
        return UserCommand.CURRENCIES;
    }

    @Override
    public SendMessage formSendMessage(long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MenuType.CURRENCY_ADD_TO_TRACK_LIST.getText())
                .replyMarkup(inlineKeyboardMarkups.get(0))
                .build();
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.CURRENCY_ADD_TO_TRACK_LIST;
    }

    @Override
    public EditMessageText formEditMessage(long chatId, int messageId, MenuCallback menuCallback) {
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(MenuType.CURRENCY_ADD_TO_TRACK_LIST.getText())
                .replyMarkup(inlineKeyboardMarkups.get(new MultipageMenuCallback(menuCallback).getPageIndex()))
                .build();
    }
}
