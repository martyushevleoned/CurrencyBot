package ru.urfu.controller.menu.trackedCurrencyMenu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.ApiService;
import ru.urfu.controller.UpdateController;
import ru.urfu.controller.constant.ButtonText;
import ru.urfu.controller.constant.ErrorMessage;
import ru.urfu.controller.constant.MenuType;
import ru.urfu.controller.menu.CallbackMenu;
import ru.urfu.exceptions.*;
import ru.urfu.model.CurrencyRequest;
import ru.urfu.utils.TextFormater;
import ru.urfu.utils.callback.menuCallback.MenuCallback;
import ru.urfu.utils.callback.menuCallback.MultipageMenuCallback;
import ru.urfu.utils.callback.menuCallback.currecncyRequestMenuCallback.CurrencyRequestFlagMenuCallback;

import java.util.List;

/**
 * Меню конкретной отслеживаемой валюты
 */
@Component
public class TrackedCurrencyMenu implements CallbackMenu {

    private final Logger LOG = LoggerFactory.getLogger(UpdateController.class);
    private final ApiService apiService;
    private final TextFormater textFormater;

    @Autowired
    public TrackedCurrencyMenu(ApiService apiService, TextFormater textFormater) {
        this.apiService = apiService;
        this.textFormater = textFormater;
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.TRACKED_CURRENCY;
    }

    @Override
    public EditMessageText formEditMessage(long chatId, int messageId, MenuCallback menuCallback) {

        CurrencyRequestFlagMenuCallback callback = new CurrencyRequestFlagMenuCallback(menuCallback);
        CurrencyRequest currencyRequest = callback.getCurrencyRequest();
        try {
            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text(textFormater.getPriceInfo(currencyRequest, apiService.getPrice(currencyRequest)))
                    .replyMarkup(getKeyboard(callback))
                    .build();

        } catch (ApiNotFoundException | ApiNotSupportedCurrencyException | SendRequestException | ParseJsonException e) {
            LOG.error("Ошибка получения стоимости валюты", e);
            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text(ErrorMessage.GET_PRICE_EXCEPTION.getText())
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
     * Сгенерировать клавиатуру для данного меню
     *
     * @param callback Callback вызвавший открытие данного меню
     * @throws CallbackException если превышен размер сериализованного объекта
     */
    private InlineKeyboardMarkup getKeyboard(CurrencyRequestFlagMenuCallback callback) throws CallbackException {

        // API телеграмма вызывает исключение если заменить меню на точно такое же меню,
        // Чтобы отправлять не идентичное меню в колбэк кнопки "Обновить курс" добавляется флаг
        // если флаг в колбэке уже есть то он убирается
        callback.invertFlag();

        return InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text(ButtonText.UPDATE_PRICE.getText())
                        .callbackData(callback.getData()).build()))
                .keyboardRow(List.of(InlineKeyboardButton.builder()
                        .text(ButtonText.BACK.getText())
                        .callbackData(new MultipageMenuCallback(MenuType.TRACKED_CURRENCY_LIST).getData()).build()))
                .build();
    }
}
