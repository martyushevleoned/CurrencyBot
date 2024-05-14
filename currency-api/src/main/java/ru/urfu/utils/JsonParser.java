package ru.urfu.utils;

import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;
import org.springframework.stereotype.Component;
import ru.urfu.exceptions.ParseJsonException;

import java.util.Objects;

/**
 * Класс для получения значений из json
 */
@Component
public class JsonParser {

    /**
     * Получить значение из json по указанному пути
     *
     * @param json представление json в виде строки. Допускает содержание пробелов и переносов строк
     * @param path путь до значения в формате: $.поле1.[индекс2].поле3
     * @return значение в виде строки
     * @throws ParseJsonException если путь в json недостижим
     */
    public String parse(String json, String path) {

        try {
            Object value = JsonPath.read(json, path);
            return Objects.toString(value);
        } catch (InvalidPathException e) {
            throw new ParseJsonException("Недостижимый путь в json");
        }
    }
}

