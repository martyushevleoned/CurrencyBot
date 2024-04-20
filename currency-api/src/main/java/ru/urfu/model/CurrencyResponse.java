package ru.urfu.model;

import java.util.Date;

/**
 * Стандартизированный ответ стоимости валюты от ApiManager
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

    @Override
    public String toString() {
        return "Response{" +
                "datetime=" + datetime +
                ", price=" + price +
                '}';
    }
}
