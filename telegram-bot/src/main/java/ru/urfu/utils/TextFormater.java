package ru.urfu.utils;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.urfu.model.CurrencyRequest;
import ru.urfu.model.CurrencyResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс для форматирования информации для меню с аналогичным текстом
 */
@Component
public class TextFormater {

    /**
     * Возвращает текст для меню отображающих стоимость валюты
     */
    public String getPriceInfo(CurrencyRequest currencyRequest, CurrencyResponse currencyResponse) {
        Date date = new Date();
        return "API: " + currencyRequest.getApi() +
                "\nдата: " + new SimpleDateFormat("dd.MM.yyyy").format(date) +
                "\nвремя: " + new SimpleDateFormat("HH:mm:ss").format(date) +
                "\n1 " + currencyRequest.getCurrency() + " = " + String.format("%.3f", currencyResponse.getPrice()) + " USD";
    }
}
