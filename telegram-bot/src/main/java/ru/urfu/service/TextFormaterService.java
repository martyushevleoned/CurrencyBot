package ru.urfu.service;

import org.springframework.stereotype.Service;
import ru.urfu.model.Request;
import ru.urfu.model.Response;

import java.util.Date;

/**
 * Класс для форматирования информации для меню с аналогичным текстом
 */
@Service
public class TextFormaterService {

    /**
     * Возвращает текст для меню отображающих стоимость валюты
     */
    public String getPriceInfo(Request request, Response response) {
        return "API: " + request.getApi() +
                "\nвремя обращения к API: " + response.getDatetime().toString() +
                "\nвремя обращения к боту: " + new Date() +
                "\n1 " + request.getCurrency() + " = " + String.format("%.3f", response.getPrice()) + " USD";
    }
}
