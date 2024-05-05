package ru.urfu.model;

import java.util.Date;

/**
 * Ответ от {@link ru.urfu.ApiManager ApiManager}, содержащий стоимость валюты и время обращения к API
 */
public class CurrencyResponse {

    private final double price;
    private final Date datetime;

    public CurrencyResponse(double price, Date datetime) {
        this.price = price;
        this.datetime = datetime;
    }

    public Date getDatetime() {
        return datetime;
    }

    public double getPrice() {
        return price;
    }
}
