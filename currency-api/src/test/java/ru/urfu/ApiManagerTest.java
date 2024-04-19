package ru.urfu;

import org.junit.Test;
import ru.urfu.model.Request;
import ru.urfu.model.Response;

import java.util.Set;

public class ApiManagerTest {

    private final ApiManager apiManager = new ApiManager();

    @Test
    public void getAllApi() {
        apiManager.getAllApi().forEach(System.out::println);
    }

    @Test
    public void getPossibleRequests() {
        Set<Request> requests = apiManager.getPossibleRequests();
        requests.forEach(System.out::println);
    }

    @Test
    public void getPrice() {
        Request request = new Request("CoinCap (Crypto)", "Tether");
        Response response = apiManager.getPrice(request);
        System.out.println(request);
        System.out.println(response);
    }

    @Test
    public void checkAllRequests() {
        Set<Request> requests = apiManager.getPossibleRequests();

        requests.forEach(r -> {
            System.out.println(r);
            System.out.println(apiManager.getPrice(r));
        });
    }
}