package ru.urfu.controller.menu.trackedCurrencyMenu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.controller.constant.ButtonText;
import ru.urfu.controller.constant.MenuType;
import ru.urfu.controller.constant.TelegramConstant;
import ru.urfu.controller.constant.UserCommand;
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
    public UserCommand getUserCommand() {
        return UserCommand.TRACK;
    }

    @Override
    public SendMessage formSendMessage(long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(MenuType.TRACKED_CURRENCY_LIST.getText())
                .replyMarkup(getInlineKeyboardMarkup(chatId, 0))
                .build();
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.TRACKED_CURRENCY_LIST;
    }

    @Override
    public EditMessageText formEditMessage(long chatId, int messageId, MenuCallback menuCallback) {
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(MenuType.TRACKED_CURRENCY_LIST.getText())
                .replyMarkup(getInlineKeyboardMarkup(chatId, new MultipageMenuCallback(menuCallback).getPageIndex()))
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
                        MenuType.TRACKED_CURRENCY_LIST,
                        TelegramConstant.MAX_COUNT_OF_ROWS
                )
        );
        rows.add(List.of(InlineKeyboardButton.builder()
                .text(ButtonText.BACK.getText())
                .callbackData(new MenuCallback(MenuType.MAIN_MENU).getData()).build()));
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
                                .callbackData(new CurrencyRequestMenuCallback(MenuType.TRACKED_CURRENCY, currencyRequest).getData())
                                .build()
                )
                .sorted(Comparator.comparing(InlineKeyboardButton::getText))
                .toList();
    }
}
