package ru.urfu.utils;

import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Класс стандартизирующий {@link CallbackQuery#getData CallbackData}
 * {@link org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton кнопок}
 */
public class Callback {

    private final String menuName;
    private final Map<String, String> flagsMap = new LinkedHashMap<>();

    /**
     * @param menuName название меню, которое будет вызвано при нажатии
     */
    public Callback(String menuName) {
        this.menuName = menuName;
    }

    /**
     * @param callbackQuery поле {@link org.telegram.telegrambots.meta.api.objects.Update обновления}
     *                      (обновление - нажатие на кнопку)
     */
    public Callback(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();

        int indexOfJson = callbackData.indexOf("{");
        menuName = callbackData.substring(0, indexOfJson);
        callbackData = callbackData.substring(indexOfJson);

        Object json = JSONValue.parse(callbackData);
        if (json instanceof JSONObject jsonObject)
            flagsMap.putAll(jsonObject);
    }

    /**
     * Добавить флаг: ключ-значение
     *
     * @param key   ключ
     * @param value значение
     */
    public void addFlag(String key, String value) {
        flagsMap.put(key, value);
    }

    /**
     * Удалить флаг по ключу
     *
     * @param key ключ
     */
    public void removeFlag(String key) {
        flagsMap.remove(key);
    }

    /**
     * Проверить наличие флага по ключу
     *
     * @param key ключ
     */
    public boolean containsFlag(String key) {
        return flagsMap.containsKey(key);
    }

    /**
     * Получить значение флага по ключу
     *
     * @param key ключ
     */
    @Nullable
    public String getFlag(String key) {
        return flagsMap.getOrDefault(key, null);
    }

    /**
     * Преобразовать в строку с сохраненим информации о всех флагах
     */
    @Override
    public String toString() {
        return menuName + new JSONObject(flagsMap);
    }
}
