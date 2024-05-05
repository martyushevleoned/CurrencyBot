package ru.urfu;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.urfu.config.BotConfig;
import ru.urfu.controller.UpdateController;

/**
 * Основной класс бота
 */
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final UpdateController updateController;

    public TelegramBot(BotConfig botConfig, UpdateController updateController) {
        this.botConfig = botConfig;
        this.updateController = updateController;
    }

    /**
     * Инициализировать {@link UpdateController}
     */
    @PostConstruct
    private void init() {
        updateController.registerTelegramBot(this);
    }

    /**
     * Получить и обработать {@link Update обновление}
     */
    @Override
    public void onUpdateReceived(Update update) {
        updateController.processUpdate(update);
    }

    /**
     * Получить имя бота
     */
    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    /**
     * Получить токен бота
     */
    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
}