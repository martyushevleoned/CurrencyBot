package ru.urfu.controller.menu.trackedCurrencyMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.controller.constant.ButtonsText;
import ru.urfu.controller.constant.MenuTypes;
import ru.urfu.controller.constant.TelegramConstants;
import ru.urfu.controller.constant.UserCommands;
import ru.urfu.controller.menu.CallbackMenu;
import ru.urfu.controller.menu.CommandMenu;
import ru.urfu.service.TrackService;
import ru.urfu.utils.MultiPageKeyboard;
import ru.urfu.utils.TextFormater;
import ru.urfu.utils.callback.MenuCallback;
import ru.urfu.utils.callback.CurrencyRequestMenuCallback;
import ru.urfu.utils.callback.MultipageMenuCallback;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Меню со списком всех отслеживаемых валют
 */
@Component
public class TrackedCurrencyListMenu implements CommandMenu, CallbackMenu {

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
    public UserCommands getUserCommand() {
        return UserCommands.TRACK;
    }

    @Override
    public SendMessage formSendMessage(Message message) {

        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(MenuTypes.TRACKED_CURRENCY_LIST.getText())
                .replyMarkup(getInlineKeyboardMarkup(message.getChatId(), 0))
                .build();
    }

    @Override
    public MenuTypes getMenuType() {
        return MenuTypes.TRACKED_CURRENCY_LIST;
    }

    @Override
    public EditMessageText formEditMessage(CallbackQuery callbackQuery) {

        return EditMessageText.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageId(callbackQuery.getMessage().getMessageId())
                .text(MenuTypes.TRACKED_CURRENCY_LIST.getText())
                .replyMarkup(getInlineKeyboardMarkup(callbackQuery.getMessage().getChatId(), new MultipageMenuCallback(callbackQuery).getPageIndex()))
                .build();
    }

    /**
     * Сгенерировать клавиатуру мультистраничного меню отслеживаемых валют.
     *
     * @param chatId    идентификатор пользователя
     * @param pageIndex индекс страницы
     */
    private InlineKeyboardMarkup getInlineKeyboardMarkup(long chatId, int pageIndex) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>(
                multiPageKeyboard.getPage(
                        pageIndex,
                        getTrackedButtons(chatId),
                        MenuTypes.TRACKED_CURRENCY_LIST,
                        TelegramConstants.MAX_COUNT_OF_ROWS
                )
        );
        rows.add(List.of(InlineKeyboardButton.builder()
                .text(ButtonsText.BACK.getText())
                .callbackData(new MenuCallback(MenuTypes.MAIN_MENU).getData()).build()));
        return new InlineKeyboardMarkup(rows);
    }

    /**
     * Получить список валют, отслеживаемых конкретным пользователем
     *
     * @param chatId идентификатор чата пользователя
     */
    private List<InlineKeyboardButton> getTrackedButtons(long chatId) {
        return trackService.getTrackedRequests(chatId).stream()
                .map(currencyRequest ->
                        InlineKeyboardButton.builder()
                                .text(textFormater.getCurrencyInfo(currencyRequest))
                                .callbackData(new CurrencyRequestMenuCallback(MenuTypes.TRACKED_CURRENCY, currencyRequest).getData())
                                .build()
                )
                .sorted(Comparator.comparing(InlineKeyboardButton::getText))
                .toList();
    }
}
