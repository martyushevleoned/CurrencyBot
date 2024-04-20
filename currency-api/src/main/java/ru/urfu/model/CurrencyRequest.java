package ru.urfu.model;

import java.util.Objects;

/**
 * Класс для стандартизированного обращения к ApiManager
 */
public class CurrencyRequest {

    private final String api;
    private final String currency;

    public CurrencyRequest(String api, String currency) {
        this.api = api;
        this.currency = currency;
    }

    public String getApi() {
        return api;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyRequest currencyRequest = (CurrencyRequest) o;
        return Objects.equals(api, currencyRequest.api) && Objects.equals(currency, currencyRequest.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(api, currency);
    }

    @Override
    public String toString() {
        return "Request{" +
                "api='" + api + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
