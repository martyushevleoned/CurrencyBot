package ru.urfu.service;

import org.springframework.stereotype.Service;
import ru.urfu.model.CurrencyRequest;
import ru.urfu.model.CurrencyResponse;

import java.util.Date;

/**
 * Класс для форматирования информации для меню с аналогичным текстом
 */
@Service
public class TextFormaterService {

    /**
     * Возвращает текст для меню отображающих стоимость валюты
     */
    public String getPriceInfo(CurrencyRequest currencyRequest, CurrencyResponse currencyResponse) {
        return "API: " + currencyRequest.getApi() +
                "\nвремя обращения к API: " + currencyResponse.getDatetime().toString() +
                "\nвремя обращения к боту: " + new Date() +
                "\n1 " + currencyRequest.getCurrency() + " = " + String.format("%.3f", currencyResponse.getPrice()) + " USD";
    }
}
