package ru.urfu.controller.menu.menus.trackedCurrencyMenu;

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
import ru.urfu.service.TrackService;
import ru.urfu.utils.Callback;
import ru.urfu.utils.MultiPageKeyboard;
import ru.urfu.utils.TextFormater;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Меню со списком всех отслеживаемых валют
 */
@Component
public class TrackedCurrencyListMenu implements SendMenu, CallbackMenu {

    private static final String CURRENT_MENU_NAME = MenuTypes.TRACKED_CURRENCY_LIST.getName();
    private static final String CURRENT_MENU_TEXT = MenuTypes.TRACKED_CURRENCY_LIST.getText();
    private static final String TRACK_COMMAND = UserCommands.TRACK.getText();

    private static final String PARENT_MENU_NAME = MenuTypes.MAIN_MENU.getName();
    private static final String CHILD_MENU_NAME = MenuTypes.TRACKED_CURRENCY.getName();

    private static final String BACK_BUTTON_TEXT = ButtonsText.BACK.getText();

    private static final String API_NAME_FLAG = Flags.API_NAME_FLAG.getName();
    private static final String CURRENCY_NAME_FLAG = Flags.CURRENCY_NAME_FLAG.getName();
    private static final String PAGE_INDEX_FLAG = Flags.PAGE_INDEX_FLAG.getName();

    private final TrackService trackService;
    private final MultiPageKeyboard multiPageKeyboard;
    private final TextFormater textFormater;

    @Autowired
    public TrackedCurrencyListMenu(TrackService trackService,
                                   MultiPageKeyboard multiPageKeyboard,
                                   TextFormater textFormater) {
        this.trackService = trackService;
        this.multiPageKeyboard = multiPageKeyboard;
        this.textFormater = textFormater;
    }

    @Override
    public boolean matchSendMessage(Message message) {
        return message.getText().equals(TRACK_COMMAND);
    }

    @Override
    public SendMessage formSendMessage(Message message) {

        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(CURRENT_MENU_TEXT)
                .replyMarkup(getInlineKeyboardMarkup(message.getChatId(), 0))
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
                .replyMarkup(getInlineKeyboardMarkup(callbackQuery.getMessage().getChatId(), pageIndex))
                .build();
    }

    /**
     * Сгенерировать клавиатуру мультистраничного меню отслеживаемых валют.
     */
    private InlineKeyboardMarkup getInlineKeyboardMarkup(long chatId, int pageIndex) {
        List<InlineKeyboardButton> buttons = getTrackedButtons(chatId);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>(multiPageKeyboard.getPage(pageIndex, buttons, CURRENT_MENU_NAME, 9));
        rows.add(List.of(InlineKeyboardButton.builder().text(BACK_BUTTON_TEXT).callbackData(PARENT_MENU_NAME).build()));
        return new InlineKeyboardMarkup(rows);
    }

    /**
     * Получить список валют, отслеживаемых конкретным пользователем
     *
     * @param chatId идентефикатор чата пользователя
     */
    private List<InlineKeyboardButton> getTrackedButtons(long chatId) {
        return trackService.getTrackedRequests(chatId).stream()
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
    }
}
