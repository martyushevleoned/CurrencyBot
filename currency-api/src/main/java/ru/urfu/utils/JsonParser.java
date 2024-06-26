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
    public String parse(String json, String path) throws ParseJsonException {
        try {
            return Objects.toString(JsonPath.read(json, path));
        } catch (InvalidPathException e) {
            throw new ParseJsonException("Недостижимый путь в json");
        }
    }

    /**
     * Получить значение из json по указанному пути
     *
     * @param json представление json в виде строки. Допускает содержание пробелов и переносов строк
     * @param path путь до значения в формате: $.поле1.[индекс2].поле3
     * @throws ParseJsonException если путь в json недостижим или поле не может быть приведено к double
     */
    public double parseDouble(String json, String path) throws ParseJsonException {
        try {
            return Double.parseDouble(parse(json, path));
        } catch (NumberFormatException e) {
            throw new ParseJsonException("Поле не может быть приведено к double");
        }
    }
}

