package ru.urfu.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Класс для получения значений из json
 */
public class JsonParser {

    /**
     * возвращает значение из json по указанному пути
     */
    public String parse(String json, String path) {
        Object object = JSONValue.parse(json);

        String[] keys = path.split("\\.");
        for (String k : keys) {
            if (isInteger(k)) {
                int index = Integer.parseInt(k);
                object = ((JSONArray) object).get(index);
            } else {
                object = ((JSONObject) object).get(k);
            }
        }
        return object.toString();
    }

    private boolean isInteger(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}

