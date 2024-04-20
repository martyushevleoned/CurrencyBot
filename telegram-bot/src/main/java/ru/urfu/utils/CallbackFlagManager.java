package ru.urfu.utils;

import org.springframework.stereotype.Component;

/**
 * Класс для стандартизированного взаимодействия с флагами в callbackData
 */
@Component
public class CallbackFlagManager {

    private final String splitSequence = "%";

    /**
     * проверяет наличие флага в тексте
     */
    public boolean containsFlag(String text){
        return text.contains(splitSequence);
    }

    /**
     * Возвращает значение флага.
     * Если флага нет то возвращает defaultValue
     */
    public String getFlagOrDefault(String text, String defaultValue) {
        if (text.contains(splitSequence)) {
            String[] parts = text.split(splitSequence);
            if (parts.length == 2)
                return parts[1];
        }
        return defaultValue;
    }

    /**
     * возвращает текст с установленным флагом
     */
    public String setFlag(String text, String value){
        return text + splitSequence + value;
    }

    /**
     * Возвращает текст без флага
     */
    public String getBody(String text){
        return text.split(splitSequence)[0];
    }
}
