package ru.urfu;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.urfu.config.BotConfig;
import ru.urfu.controller.UpdateController;

/**
 * Основной класс бота
 */
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final UpdateController updateController;

    @Autowired
    public TelegramBot(BotConfig botConfig, UpdateController updateController) {
        super(botConfig.getToken());
        this.botConfig = botConfig;
        this.updateController = updateController;
    }

    /**
     * Инициализировать {@link UpdateController}
     */
    @PostConstruct
    private void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(this);
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
}