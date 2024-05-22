package ru.urfu.controller;

import org.junit.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
 * Тестирование обработки текстовых команд классом {@link UpdateController}
 */
public class UpdateControllerCommandTest {

    private final UpdateController updateController;
    private final AbsSender sender;

    private final TextFormater textFormater = new TextFormater();
    private final CurrencyRequest firstCurrencyRequest = new CurrencyRequest("API 1", "currency 1");
    private final CurrencyRequest secondCurrencyRequest = new CurrencyRequest("API 2", "currency 2");

    /**
     * Инициализировать {@link UpdateController}
     */
    public UpdateControllerCommandTest() {
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
        updateController = new UpdateController(List.of(
                new MainMenu(),
                new TrackedCurrencyListMenu(trackService, multiPageKeyboard, textFormater),
                new CurrencyListMenu(apiService, multiPageKeyboard, textFormater),
                new ApiListMenu(apiService)
        ), List.of(), sender);
    }

    /**
     * Тестирование метода {@link UpdateController#processUpdate} при получении текстовой команды /start
     * <ul>
     *     <li>Создаём {@link Update} с текстовой командой</li>
     *     <li>Обрабатываем обновление</li>
     *     <li>Проверяем соответствие полей отправленного сообщения
     *     <ol>
     *         <li>Идентификатор пользователя</li>
     *         <li>Текст сообщения</li>
     *         <li>Кнопки (текст и {@link CallbackQuery#getData() колбэк})</li>
     *     </ol>
     *     </li>
     * </ul>
     */
    @Test
    public void mainMenuCommandTest() throws TelegramApiException {

        Update update = generateCommandUpdate("/start", 1L);
        updateController.processUpdate(update);

        Mockito.verify(sender, Mockito.times(1)).execute(Mockito.eq(SendMessage.builder()
                .chatId("1")
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
     * Тестирование метода {@link UpdateController#processUpdate} при получении текстовой команды /track
     * <ul>
     *     <li>Создаём {@link Update} с текстовой командой</li>
     *     <li>Обрабатываем обновление</li>
     *     <li>Проверяем соответствие полей отправленного сообщения
     *     <ol>
     *         <li>Идентификатор пользователя</li>
     *         <li>Текст сообщения</li>
     *         <li>Кнопки (текст и {@link CallbackQuery#getData() колбэк})</li>
     *     </ol>
     *     </li>
     * </ul>
     */
    @Test
    public void trackedCurrencyListMenuCommandTest() throws TelegramApiException {

        Update update = generateCommandUpdate("/track", 2L);
        updateController.processUpdate(update);

        Mockito.verify(sender, Mockito.times(1)).execute(Mockito.eq(SendMessage.builder()
                .chatId("2")
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
     * Тестирование метода {@link UpdateController#processUpdate} при получении текстовой команды /currencies
     * <ul>
     *     <li>Создаём {@link Update} с текстовой командой</li>
     *     <li>Обрабатываем обновление</li>
     *     <li>Проверяем соответствие полей отправленного сообщения
     *     <ol>
     *         <li>Идентификатор пользователя</li>
     *         <li>Текст сообщения</li>
     *         <li>Кнопки (текст и {@link CallbackQuery#getData() колбэк})</li>
     *     </ol>
     *     </li>
     * </ul>
     */
    @Test
    public void currencyListMenuCommandTest() throws TelegramApiException {

        Update update = generateCommandUpdate("/currencies", 3L);
        updateController.processUpdate(update);

        Mockito.verify(sender, Mockito.times(1)).execute(Mockito.eq(SendMessage.builder()
                .chatId("3")
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
     * Тестирование метода {@link UpdateController#processUpdate} при получении текстовой команды /api
     * <ul>
     *     <li>Создаём {@link Update} с текстовой командой</li>
     *     <li>Обрабатываем обновление</li>
     *     <li>Проверяем соответствие полей отправленного сообщения
     *     <ol>
     *         <li>Идентификатор пользователя</li>
     *         <li>Текст сообщения</li>
     *         <li>Кнопки (текст и {@link CallbackQuery#getData() колбэк})</li>
     *     </ol>
     *     </li>
     * </ul>
     */
    @Test
    public void apiListMenuCommandTest() throws TelegramApiException {

        Update update = generateCommandUpdate("/api", 4L);
        updateController.processUpdate(update);

        Mockito.verify(sender, Mockito.times(1)).execute(Mockito.eq(SendMessage.builder()
                .chatId("4")
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
     * Создать {@link Update обновление} c текстовой командой
     *
     * @param command текст команды
     * @param chatId  идентификатор пользователя
     */
    private Update generateCommandUpdate(String command, long chatId) {
        Message message = Mockito.mock(Message.class);
        Mockito.when(message.hasText()).thenReturn(true);
        Mockito.when(message.getText()).thenReturn(command);
        Mockito.when(message.getChatId()).thenReturn(chatId);

        Update update = Mockito.mock(Update.class);
        Mockito.when(update.hasMessage()).thenReturn(true);
        Mockito.when(update.getMessage()).thenReturn(message);
        return update;
    }
}