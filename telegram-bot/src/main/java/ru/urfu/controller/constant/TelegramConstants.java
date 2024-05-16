package ru.urfu.controller.constant;

/**
 *  Константы ограничений API телеграмма
 */
public interface TelegramConstants {

    /**
     * Максимальное количество рядов кнопок в {@link org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup}
     */
    int MAX_COUNT_OF_ROWS = 10;

    /**
     * Максимальная длинна {@link org.telegram.telegrambots.meta.api.objects.CallbackQuery#setData CallbackData}
     */
    int MAX_CALLBACK_DATA_LENGTH = 60;
}
