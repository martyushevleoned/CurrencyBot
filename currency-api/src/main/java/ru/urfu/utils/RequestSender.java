package ru.urfu.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * Класс для выполнения http запросов
 */
public class RequestSender {

    private final OkHttpClient client = new OkHttpClient().newBuilder().build();

    /**
     * Выполняет get-запрос по указанному адресу и возвращает тело отклика
     *
     * @param url адрес, по которому нужно отправить запрос
     * @return тело отклика, если отсклик не получен возвращает null
     */
    @Nullable
    public String sendGetRequest(String url) {
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            return responseBody == null
                    ? null
                    : responseBody.string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
