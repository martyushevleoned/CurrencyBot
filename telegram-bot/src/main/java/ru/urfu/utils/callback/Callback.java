package ru.urfu.utils.callback;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.urfu.exceptions.CallbackException;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Класс для сериализации и десериализации опций для передачи в {@link CallbackQuery#setData CallbackData}
 * {@link org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton кнопок}
 */
class Callback implements Serializable {

    private final Map<String, String> optionsMap;

    public Callback() {
        optionsMap = new LinkedHashMap<>();
    }

    /**
     * Десериализовать опции из {@link CallbackQuery}
     *
     * @param callbackQuery поле {@link org.telegram.telegrambots.meta.api.objects.Update обновления}
     *                      (обновление - нажатие на кнопку)
     */
    public Callback(CallbackQuery callbackQuery) {
        optionsMap = JsonPath.parse(callbackQuery.getData()).read("$");
    }

    /**
     * Добавить опцию
     *
     * @param option опция
     * @param value  значение
     */
    protected void addOption(Options option, String value) {
        optionsMap.put(option.getOption(), value);
    }

    /**
     * Удалить опцию
     *
     * @param option опция
     */
    protected void removeOption(Options option) {
        optionsMap.remove(option.getOption());
    }

    /**
     * Проверить наличие опции
     *
     * @param option опция
     */
    protected boolean containsOption(Options option) {
        return optionsMap.containsKey(option.getOption());
    }

    /**
     * Получить значение опции
     *
     * @param option опция
     * @throws CallbackException если опция отсутствует
     */
    protected String getOption(Options option) {
        if (optionsMap.containsKey(option.getOption()))
            return optionsMap.get(option.getOption());
        throw new CallbackException("Опция %s отсутствует".formatted(option.name()));
    }

    /**
     * Cериализовать в строку с сохранением информации о всех опциях
     * @throws CallbackException если длинна возвращаемой строки превышает ограничение api телеграмма
     */
    public String getData() {
        DocumentContext documentContext = JsonPath.parse("{}");
        optionsMap.forEach((k, v) -> documentContext.put("$", k, v));
        String json = documentContext.jsonString();
        if (json.length() >= 60)
            throw new CallbackException("Превышен допустимый размер CallbackData");
        return json;
    }
}
