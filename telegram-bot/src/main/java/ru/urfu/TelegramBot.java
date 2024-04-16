package ru.urfu;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private BotConfig botConfig;

    @Autowired
    private UpdateController updateController;

    /**
     * Передаёт ссылку на бота в updateController
     */
    @PostConstruct
    private void init(){
        updateController.registerTelegramBot(this);
    }

    /**
     * Вызывается при обновлении сообщений бота и передаёт обновления в обработчик
     */
    @Override
    public void onUpdateReceived(Update update) {
        updateController.processUpdate(update);
    }

    /**
     * Возвращает имя бота
     */
    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    /**
     * Возвращает токен бота
     */
    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
}