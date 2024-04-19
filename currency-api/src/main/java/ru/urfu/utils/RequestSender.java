package ru.urfu.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Класс для обращения к API по http запросу
 */
public class RequestSender {

    private final OkHttpClient client = new OkHttpClient().newBuilder().build();

    /**
     * Выполняет get-запрос по указанному адресу и возвращает тело отклика
     */
    public String send(String url) {
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
