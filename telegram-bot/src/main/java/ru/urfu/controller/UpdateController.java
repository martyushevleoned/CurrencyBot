package ru.urfu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.urfu.TelegramBot;
import ru.urfu.controller.menu.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Обрабатывает все обновления бота
 */
@Component
public class UpdateController {

    private TelegramBot bot;
    private List<Menu> menuList;

    @Autowired
    private MainMenu mainMenu;
    @Autowired
    private CurrencyListMenu currencyListMenu;
    @Autowired
    private TrackedCurrencyListMenu trackedCurrencyListMenu;
    @Autowired
    private CurrencyMenu currencyMenu;
    @Autowired
    private TrackedCurrencyMenu trackedCurrencyMenu;

    /**
     * Вызывается единожды при инициализации.
     * Инициализирует TelegramBot.
     * Собирает все обработчики (Menu) в лист.
     */
    public void registerTelegramBot(TelegramBot telegramBot) {
        this.bot = telegramBot;
        menuList = new ArrayList<>();
        menuList.add(mainMenu);
        menuList.add(currencyListMenu);
        menuList.add(trackedCurrencyListMenu);
        menuList.add(currencyMenu);
        menuList.add(trackedCurrencyMenu);
    }

    /**
     * Определяет тип обновления:
     * текстовое сообщение / нажатие на кнопку
     */
    public void processUpdate(Update update) {
        try {

            if (update.hasMessage() && update.getMessage().hasText()) {
                processMessage(update.getMessage());

            } else if (update.hasCallbackQuery()) {
                processCallback(update.getCallbackQuery());
            }

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * обрабатывает текстовые сообщения
     */
    private void processMessage(Message message) throws TelegramApiException {
        for (Menu m : menuList) {
            if (m.matchSendMessage(message)){
                bot.execute(m.getSendMessage(message));
                break;
            }
        }
    }

    /**
     * обрабатывает нажатия на кнопки
     */
    private void processCallback(CallbackQuery callback) throws TelegramApiException {
        for (Menu m : menuList) {
            if (m.matchEditMessage(callback)){
                bot.execute(m.getEditMessage(callback));
                break;
            }
        }
    }
}