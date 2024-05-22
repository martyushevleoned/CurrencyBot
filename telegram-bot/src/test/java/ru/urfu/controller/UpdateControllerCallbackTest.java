package ru.urfu.controller;

import org.junit.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.urfu.ApiService;
import ru.urfu.controller.constant.MenuType;
import ru.urfu.controller.menu.MainMenu;
import ru.urfu.controller.menu.apiMenu.ApiListMenu;
import ru.urfu.controller.menu.currencyMenu.CurrencyListMenu;
import ru.urfu.controller.menu.trackedCurrencyMenu.TrackedCurrencyListMenu;
import ru.urfu.model.CurrencyRequest;
import ru.urfu.service.TrackService;
import ru.urfu.utils.MultiPageKeyboard;
import ru.urfu.utils.TextFormater;
import ru.urfu.utils.callback.menuCallback.ApiMenuCallback;
import ru.urfu.utils.callback.menuCallback.currecncyRequestMenuCallback.CurrencyRequestMenuCallback;
import ru.urfu.utils.callback.menuCallback.MenuCallback;
import ru.urfu.utils.callback.menuCallback.MultipageMenuCallback;

import java.util.List;
import java.util.Set;

/**
 * Тестирование обработки нажатия кнопки классом {@link UpdateController}
 */
public class UpdateControllerCallbackTest {

    private final UpdateController updateController;
    private final AbsSender sender;

    private final TextFormater textFormater = new TextFormater();
    private final CurrencyRequest firstCurrencyRequest = new CurrencyRequest("API 1", "currency 1");
    private final CurrencyRequest secondCurrencyRequest = new CurrencyRequest("API 2", "currency 2");

    /**
     * Инициализировать {@link UpdateController}
     */
    public UpdateControllerCallbackTest() {
        ApiService apiService = Mockito.mock(ApiService.class);
        Mockito.when(apiService.getPossibleRequests()).thenReturn(Set.of(
                firstCurrencyRequest,
                secondCurrencyRequest
        ));
        Mockito.when(apiService.getAllApiNames()).thenReturn(Set.of(
                firstCurrencyRequest.api(),
                secondCurrencyRequest.api()
        ));

        TrackService trackService = Mockito.mock(TrackService.class);
        Mockito.when(trackService.getTrackedRequests(2)).thenReturn(Set.of(
                firstCurrencyRequest,
                secondCurrencyRequest
        ));

        sender = Mockito.mock(AbsSender.class);
        MultiPageKeyboard multiPageKeyboard = new MultiPageKeyboard();
        updateController = new UpdateController(List.of(), List.of(
                new MainMenu(),
                new TrackedCurrencyListMenu(trackService, multiPageKeyboard, textFormater),
                new CurrencyListMenu(apiService, multiPageKeyboard, textFormater),
                new ApiListMenu(apiService)
        ), sender);
    }

    /**
     * Тестирование метода {@link UpdateController#processUpdate} при открытии главного меню кнопкой
     * <ul>
     *     <li>Создаём {@link Update} c {@link CallbackQuery}</li>
     *     <li>Обрабатываем обновление</li>
     *     <li>Проверяем соответствие полей изменённого сообщения
     *     <ol>
     *         <li>Идентификатор пользователя и сообщения</li>
     *         <li>Текст сообщения</li>
     *         <li>Кнопки (текст и {@link CallbackQuery#getData() колбэк})</li>
     *     </ol>
     *     </li>
     * </ul>
     */
    @Test
    public void mainMenuCallbackTest() throws TelegramApiException {

        Update update = generateUpdate(1L, 2, new MenuCallback(MenuType.MAIN_MENU).getData());
        updateController.processUpdate(update);

        Mockito.verify(sender, Mockito.times(1)).execute(Mockito.eq(EditMessageText.builder()
                .chatId("1")
                .messageId(2)
                .text("Главное меню")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .text("Все валюты")
                                .callbackData(new MultipageMenuCallback(MenuType.CURRENCY_ADD_TO_TRACK_LIST).getData())
                                .build()))
                        .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .text("Отслеживаемые валюты")
                                .callbackData(new MultipageMenuCallback(MenuType.TRACKED_CURRENCY_LIST).getData())
                                .build()))
                        .build()).build()));
    }

    /**
     * Тестирование метода {@link UpdateController#processUpdate} при открытии меню "Отслеживаемые валюты" кнопкой
     * <ul>
     *     <li>Создаём {@link Update} c {@link CallbackQuery}</li>
     *     <li>Обрабатываем обновление</li>
     *     <li>Проверяем соответствие полей изменённого сообщения
     *     <ol>
     *         <li>Идентификатор пользователя и сообщения</li>
     *         <li>Текст сообщения</li>
     *         <li>Кнопки (текст и {@link CallbackQuery#getData() колбэк})</li>
     *     </ol>
     *     </li>
     * </ul>
     */
    @Test
    public void trackedCurrencyListMenuCallbackTest() throws TelegramApiException {

        Update update = generateUpdate(2L, 3, new MultipageMenuCallback(MenuType.TRACKED_CURRENCY_LIST).getData());
        updateController.processUpdate(update);

        Mockito.verify(sender, Mockito.times(1)).execute(Mockito.eq(EditMessageText.builder()
                .chatId("2")
                .messageId(3)
                .text("Отслеживаемые валюты")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .text(textFormater.getCurrencyInfo(firstCurrencyRequest))
                                .callbackData(new CurrencyRequestMenuCallback(MenuType.TRACKED_CURRENCY, firstCurrencyRequest).getData())
                                .build()))
                        .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .text(textFormater.getCurrencyInfo(secondCurrencyRequest))
                                .callbackData(new CurrencyRequestMenuCallback(MenuType.TRACKED_CURRENCY, secondCurrencyRequest).getData())
                                .build()))
                        .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .text("Назад")
                                .callbackData(new MenuCallback(MenuType.MAIN_MENU).getData())
                                .build()))
                        .build()).build()));
    }

    /**
     * Тестирование метода {@link UpdateController#processUpdate} при открытии меню "Все валюты" кнопкой
     * <ul>
     *     <li>Создаём {@link Update} c {@link CallbackQuery}</li>
     *     <li>Обрабатываем обновление</li>
     *     <li>Проверяем соответствие полей изменённого сообщения
     *     <ol>
     *         <li>Идентификатор пользователя и сообщения</li>
     *         <li>Текст сообщения</li>
     *         <li>Кнопки (текст и {@link CallbackQuery#getData() колбэк})</li>
     *     </ol>
     *     </li>
     * </ul>
     */
    @Test
    public void currencyListMenuCallbackTest() throws TelegramApiException {

        Update update = generateUpdate(3L, 4, new MultipageMenuCallback(MenuType.CURRENCY_ADD_TO_TRACK_LIST).getData());
        updateController.processUpdate(update);

        Mockito.verify(sender, Mockito.times(1)).execute(Mockito.eq(EditMessageText.builder()
                .chatId("3")
                .messageId(4)
                .text("Все валюты")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .text(textFormater.getCurrencyInfo(firstCurrencyRequest))
                                .callbackData(new CurrencyRequestMenuCallback(MenuType.CURRENCY_ADD_TO_TRACK, firstCurrencyRequest).getData())
                                .build()))
                        .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .text(textFormater.getCurrencyInfo(secondCurrencyRequest))
                                .callbackData(new CurrencyRequestMenuCallback(MenuType.CURRENCY_ADD_TO_TRACK, secondCurrencyRequest).getData())
                                .build()))
                        .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .text("Назад")
                                .callbackData(new MenuCallback(MenuType.MAIN_MENU).getData())
                                .build()))
                        .build()).build()));
    }

    /**
     * Тестирование метода {@link UpdateController#processUpdate} при открытии меню "Все API" кнопкой
     * <ul>
     *     <li>Создаём {@link Update} c {@link CallbackQuery}</li>
     *     <li>Обрабатываем обновление</li>
     *     <li>Проверяем соответствие полей изменённого сообщения
     *     <ol>
     *         <li>Идентификатор пользователя и сообщения</li>
     *         <li>Текст сообщения</li>
     *         <li>Кнопки (текст и {@link CallbackQuery#getData() колбэк})</li>
     *     </ol>
     *     </li>
     * </ul>
     */
    @Test
    public void apiListMenuCallbackTest() throws TelegramApiException {

        Update update = generateUpdate(4L, 5, new MenuCallback(MenuType.API_LIST).getData());
        updateController.processUpdate(update);

        Mockito.verify(sender, Mockito.times(1)).execute(Mockito.eq(EditMessageText.builder()
                .chatId("4")
                .messageId(5)
                .text("Все API")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .text(firstCurrencyRequest.api())
                                .callbackData(new ApiMenuCallback(MenuType.API, firstCurrencyRequest.api()).getData())
                                .build()))
                        .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .text(secondCurrencyRequest.api())
                                .callbackData(new ApiMenuCallback(MenuType.API, secondCurrencyRequest.api()).getData())
                                .build()))
                        .build()).build()));
    }

    /**
     * Создать {@link Update обновление} нажатия на кнопку
     *
     * @param chatId       идентификатор пользователя
     * @param messageId    идентификатор сообщения
     * @param callbackData текст возвращаемый в {@link Update обновление} по нажатию кнопки
     */
    private Update generateUpdate(long chatId, int messageId, String callbackData) {

        Message message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(chatId);
        Mockito.when(message.getMessageId()).thenReturn(messageId);

        CallbackQuery callbackQuery = Mockito.mock(CallbackQuery.class);
        Mockito.when(callbackQuery.getMessage()).thenReturn(message);
        Mockito.when(callbackQuery.getData()).thenReturn(callbackData);

        Update update = Mockito.mock(Update.class);
        Mockito.when(update.hasMessage()).thenReturn(false);
        Mockito.when(update.hasCallbackQuery()).thenReturn(true);
        Mockito.when(update.getCallbackQuery()).thenReturn(callbackQuery);
        return update;
    }
}