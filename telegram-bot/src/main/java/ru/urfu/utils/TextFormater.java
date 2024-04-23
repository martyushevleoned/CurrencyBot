package ru.urfu.utils;

import org.springframework.stereotype.Component;
import ru.urfu.model.CurrencyRequest;
import ru.urfu.model.CurrencyResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс для форматирования текста для меню с аналогичной информацией
 */
@Component
public class TextFormater {

    /**
     * Получить текст для кнопки, ведущей в меню конкретной валюты
     *
     * @param currencyRequest запрос к ApiService
     * @return текст сообщения
     */
    public String getCurrencyInfo(CurrencyRequest currencyRequest) {
        return "%s - %s"
                .formatted(
                        currencyRequest.getApi(),
                        currencyRequest.getCurrency()
                );
    }

    /**
     * Получить текст для меню, отображающих стоимость валюты
     *
     * @param currencyRequest  запрос к ApiService
     * @param currencyResponse ответ от ApiService
     * @return текст сообщения
     */
    public String getPriceInfo(CurrencyRequest currencyRequest, CurrencyResponse currencyResponse) {

        Date date = new Date();
        return """
                API: %s
                дата: %s
                время: %s
                1 %s = %s USD
                """
                .formatted(
                        currencyRequest.getApi(),
                        new SimpleDateFormat("dd.MM.yyyy").format(date),
                        new SimpleDateFormat("HH:mm").format(date),
                        currencyRequest.getCurrency(),
                        String.format("%.3f", currencyResponse.getPrice())
                );
    }
}
