package ru.urfu.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.urfu.controller.constant.MenuType;
import ru.urfu.controller.menu.CallbackMenu;
import ru.urfu.controller.menu.CommandMenu;
import ru.urfu.utils.callback.MenuCallback;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Компонент для обработки обновления бота
 */
@Component
public class UpdateController {

    private final Logger LOG = LoggerFactory.getLogger(UpdateController.class);
    /**
     * Коллекция сопоставляющая текстовые команды (например /start) и класс для обработки данной команды
     */
    private final Map<String, CommandMenu> commandMenuMap;
    /**
     * Коллекция сопоставляющая {@link MenuType#getMenuName название меню} и класс для обработки {@link Update обновлений} данного меню
     */
    private final Map<String, CallbackMenu> callbackMenuMap;
    private final AbsSender sender;

    @Autowired
    public UpdateController(List<CommandMenu> commandMenus, List<CallbackMenu> callbackMenus, @Lazy AbsSender sender) {
        commandMenuMap = commandMenus.stream().collect(Collectors.toUnmodifiableMap(commandMenu -> commandMenu.getUserCommand().getCommand(), Function.identity()));
        callbackMenuMap = callbackMenus.stream().collect(Collectors.toUnmodifiableMap(callbackMenu -> callbackMenu.getMenuType().getMenuName(), Function.identity()));
        this.sender = sender;
    }

    /**
     * Обработать обновление
     */
    public void processUpdate(Update update) {

        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                processMessage(update.getMessage());
            } else if (update.hasCallbackQuery()) {
                processCallback(update.getCallbackQuery());
            }
        } catch (TelegramApiException e) {
            LOG.error("Ошибка отправки сообщения", e);
        }

    }

    /**
     * Обработать текстовые сообщения
     */
    private void processMessage(Message message) throws TelegramApiException {
        if (commandMenuMap.containsKey(message.getText()))
            sender.execute(commandMenuMap.get(message.getText()).formSendMessage(message.getChatId()));
    }

    /**
     * Обработать нажатия на кнопки
     */
    private void processCallback(CallbackQuery callbackQuery) throws TelegramApiException {
        MenuCallback menuCallback = new MenuCallback(callbackQuery);
        if (callbackMenuMap.containsKey(menuCallback.getMenuName()))
            sender.execute(callbackMenuMap.get(menuCallback.getMenuName()).formEditMessage(
                    callbackQuery.getMessage().getChatId(),
                    callbackQuery.getMessage().getMessageId(),
                    menuCallback));
    }
}