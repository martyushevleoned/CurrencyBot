package ru.urfu.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.urfu.TelegramBot;
import ru.urfu.controller.menu.menus.MainMenu;
import ru.urfu.controller.menu.menus.apiMenu.ApiListMenu;
import ru.urfu.controller.menu.menus.apiMenu.ApiMenu;
import ru.urfu.controller.menu.menus.currencyMenu.CurrencyListMenu;
import ru.urfu.controller.menu.menus.currencyMenu.CurrencyMenu;
import ru.urfu.controller.menu.menus.trackedCurrencyMenu.TrackedCurrencyListMenu;
import ru.urfu.controller.menu.menus.trackedCurrencyMenu.TrackedCurrencyMenu;
import ru.urfu.controller.menu.CallbackMenu;
import ru.urfu.controller.menu.Menu;
import ru.urfu.controller.menu.SendMenu;

import java.util.List;

/**
 * Компонент для обработки обновления бота
 */
@Component
public class UpdateController {

    private final static Logger LOG = LoggerFactory.getLogger(UpdateController.class);
    private TelegramBot bot;
    private final List<Menu> menus;

    @Autowired
    public UpdateController(MainMenu mainMenu,
                            CurrencyListMenu currencyListMenu,
                            CurrencyMenu currencyMenu,
                            TrackedCurrencyListMenu trackedCurrencyListMenu,
                            TrackedCurrencyMenu trackedCurrencyMenu,
                            ApiListMenu apiListMenu,
                            ApiMenu apiMenu) {
        menus = List.of(
                mainMenu,
                currencyListMenu,
                currencyMenu,
                trackedCurrencyListMenu,
                trackedCurrencyMenu,
                apiListMenu,
                apiMenu
        );
    }

    /**
     * Инициализировать Telegram bot.<br>
     * <b>Вызывается единожды при инициализации.</b>
     */
    public void registerTelegramBot(TelegramBot telegramBot) {
        this.bot = telegramBot;
    }

    /**
     * Обработать обновления
     */
    public void processUpdate(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            processMessage(update.getMessage());

        } else if (update.hasCallbackQuery()) {
            processCallback(update.getCallbackQuery());
        }
    }

    /**
     * Обработать текстовые сообщения
     */
    private void processMessage(Message message) {

        for (Menu menu : menus) {
            if (menu instanceof SendMenu sendMenu) {
                if (sendMenu.matchSendMessage(message)) {
                    try {
                        bot.execute(sendMenu.formSendMessage(message));
                    } catch (TelegramApiException e) {
                        LOG.error(e.getMessage());
                    }
                    break;
                }
            }
        }
    }

    /**
     * Обработать нажатия на кнопки
     */
    private void processCallback(CallbackQuery callbackQuery) {
        for (Menu menu : menus) {
            if (menu instanceof CallbackMenu callbackMenu){
                if (callbackMenu.matchEditMessage(callbackQuery)) {
                    try {
                        bot.execute(callbackMenu.formEditMessage(callbackQuery));
                    } catch (TelegramApiException e) {
                        LOG.error(e.getMessage());
                    }
                    break;
                }
            }
        }
    }
}