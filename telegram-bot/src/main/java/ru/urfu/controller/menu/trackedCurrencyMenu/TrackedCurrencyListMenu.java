package ru.urfu.controller.menu.trackedCurrencyMenu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.controller.UpdateController;
import ru.urfu.controller.constant.*;
import ru.urfu.controller.menu.CallbackMenu;
import ru.urfu.controller.menu.CommandMenu;
import ru.urfu.exceptions.CallbackException;
import ru.urfu.service.TrackService;
import ru.urfu.utils.MultiPageKeyboard;
import ru.urfu.utils.TextFormater;
import ru.urfu.utils.callback.menuCallback.MenuCallback;
import ru.urfu.utils.callback.menuCallback.currecncyRequestMenuCallback.CurrencyRequestMenuCallback;
import ru.urfu.utils.callback.menuCallback.MultipageMenuCallback;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Меню со списком всех отслеживаемых валют
 */
@Component
public class TrackedCurrencyListMenu implements CommandMenu, CallbackMenu {

    private final Logger LOG = LoggerFactory.getLogger(UpdateController.class);
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
        try {
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(MenuType.TRACKED_CURRENCY_LIST.getText())
                    .replyMarkup(getInlineKeyboardMarkup(chatId, 0))
                    .build();
        } catch (CallbackException e) {
            LOG.error("Ошибка создания кнопки", e);
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(ErrorMessage.CALLBACK_EXCEPTION.getText())
                    .build();
        }
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.TRACKED_CURRENCY_LIST;
    }

    @Override
    public EditMessageText formEditMessage(long chatId, int messageId, MenuCallback menuCallback) {
        try {
            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text(MenuType.TRACKED_CURRENCY_LIST.getText())
                    .replyMarkup(getInlineKeyboardMarkup(chatId, new MultipageMenuCallback(menuCallback).getPageIndex()))
                    .build();
        } catch (CallbackException e) {
            LOG.error("Ошибка создания кнопки", e);
            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text(ErrorMessage.CALLBACK_EXCEPTION.getText())
                    .build();
        }
    }

    /**
     * Сгенерировать клавиатуру мультистраничного меню отслеживаемых валют.
     *
     * @param chatId    идентификатор пользователя
     * @param pageIndex индекс страницы
     * @throws CallbackException если невозможно инициализировать хотя бы одну кнопку
     */
    private InlineKeyboardMarkup getInlineKeyboardMarkup(long chatId, int pageIndex) throws CallbackException {
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
     * @throws CallbackException если превышен размер {@link CurrencyRequestMenuCallback сериализуеммого объекта}
     */
    private List<InlineKeyboardButton> getTrackedButtons(long chatId) throws CallbackException {
        return trackService.getTrackedRequests(chatId).stream()
                .map(currencyRequest -> InlineKeyboardButton.builder()
                        .text(textFormater.getCurrencyInfo(currencyRequest))
                        .callbackData(new CurrencyRequestMenuCallback(MenuType.TRACKED_CURRENCY, currencyRequest).getData())
                        .build()
                )
                .sorted(Comparator.comparing(InlineKeyboardButton::getText))
                .toList();
    }
}
