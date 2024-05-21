package ru.urfu.utils.callback;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.urfu.controller.constant.TelegramConstant;
import ru.urfu.exceptions.CallbackException;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Абстрактный класс для {@link Callback#getData сериализации} и {@link Callback#Callback(CallbackQuery) десериализации} опций для передачи в {@link CallbackQuery#setData CallbackData}
 * {@link org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton кнопок}
 */
abstract class Callback implements Serializable {

    /**
     * TODO
     */
    private final Map<String, String> options;

    /**
     * TODO
     */
    Callback() {
        options = new LinkedHashMap<>();
    }

    /**
     * Десериализовать опции из {@link CallbackQuery}
     *
     * @param callbackQuery поле {@link org.telegram.telegrambots.meta.api.objects.Update обновления}
     *                      (обновление - нажатие на кнопку)
     */
    Callback(CallbackQuery callbackQuery) {
        options = JsonPath.parse(callbackQuery.getData()).read("$");
    }

    /**
     * TODO
     */
    Callback(Callback callback) {
        options = callback.getOptions();
    }

    /**
     * TODO
     */
    Map<String, String> getOptions() {
        return options;
    }

    /**
     * Добавить опцию
     *
     * @param option опция
     * @param value  значение
     */
    void addOption(Option option, String value) {
        options.put(option.getOption(), value);
    }

    /**
     * Удалить опцию
     *
     * @param option опция
     */
    void removeOption(Option option) {
        options.remove(option.getOption());
    }

    /**
     * Проверить наличие опции
     *
     * @param option опция
     */
    boolean containsOption(Option option) {
        return options.containsKey(option.getOption());
    }

    /**
     * Получить значение опции
     *
     * @param option опция
     * @throws CallbackException если опция отсутствует
     */
    String getOption(Option option) {
        if (options.containsKey(option.getOption()))
            return options.get(option.getOption());
        throw new CallbackException("Опция %s отсутствует".formatted(option.name()));
    }

    /**
     * Cериализовать в строку с сохранением информации о всех опциях
     * @throws CallbackException если длинна возвращаемой строки превышает ограничение api телеграмма
     */
    public String getData() {
        DocumentContext documentContext = JsonPath.parse("{}");
        options.forEach((option, value) -> documentContext.put("$", option, value));
        String json = documentContext.jsonString();
        if (json.length() >= TelegramConstant.MAX_CALLBACK_DATA_LENGTH)
            throw new CallbackException("Превышен допустимый размер CallbackData");
        return json;
    }
}
