package ru.urfu.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import ru.urfu.exceptions.ParseJsonException;

/**
 * Класс для получения значений из json
 */
public class JsonParser {

    /**
     * Получить значение из json по указанному пути
     *
     * @param json представление json в виде строки. допускает содержание пробелов и переносов строк
     * @param path путь до значения в формате: поле1.поле2.поле3
     * @return значение в виде строки
     */
    public String parse(String json, String path) {
        Object object = JSONValue.parse(json);

        String[] keys = path.split("\\.");
        for (String key : keys) {

            if (isInteger(key)) {
                int index = Integer.parseInt(key);
                if (object instanceof JSONArray jsonArray)
                    object = jsonArray.get(index);
                else
                    throw new ParseJsonException("Недостижимый путь в json");

            } else {
                if (object instanceof JSONObject jsonObject)
                    object = jsonObject.get(key);
                else
                    throw new ParseJsonException("Недостижимый путь в json");
            }
        }

        return object == null
                ? "null"
                : object.toString();
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

