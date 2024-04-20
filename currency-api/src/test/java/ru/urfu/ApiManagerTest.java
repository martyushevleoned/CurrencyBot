package ru.urfu;

import org.junit.Test;
import ru.urfu.model.CurrencyRequest;
import ru.urfu.model.CurrencyResponse;

import java.util.Set;

public class ApiManagerTest {

    private final ApiManager apiManager = new ApiManager();

    @Test
    public void getAllApiDescription() {
        apiManager.getAllApi().forEach(apiName -> System.out.println(apiName + ": " + apiManager.getDescription(apiName)));
    }

    @Test
    public void getPossibleRequests() {
        Set<CurrencyRequest> currencyRequests = apiManager.getPossibleRequests();
        currencyRequests.forEach(System.out::println);
    }

    @Test
    public void getPrice() {
        CurrencyRequest currencyRequest = new CurrencyRequest("CoinCap API", "Tether");
        CurrencyResponse currencyResponse = apiManager.getPrice(currencyRequest);
        System.out.println(currencyRequest);
        System.out.println(currencyResponse);
    }

    @Test
    public void checkAllRequests() {
        Set<CurrencyRequest> currencyRequests = apiManager.getPossibleRequests();

        currencyRequests.forEach(r -> {
            System.out.println(r);
            System.out.println(apiManager.getPrice(r));
        });
    }
}