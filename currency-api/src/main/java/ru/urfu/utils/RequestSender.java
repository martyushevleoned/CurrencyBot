package ru.urfu.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Component;
import ru.urfu.exceptions.SendRequestException;

import java.io.IOException;

/**
 * Класс для выполнения http запросов
 */
@Component
public class RequestSender {

    private final OkHttpClient client = new OkHttpClient().newBuilder().build();

    /**
     * Выполняет get-запрос по указанному адресу и возвращает тело отклика
     *
     * @param url адрес, по которому нужно отправить запрос
     * @return тело отклика, если отклик не получен возвращает null
     * @throws SendRequestException если невозможно получить тело отклика или возникла ошибка при выполнении запроса
     */
    public String sendGetRequest(String url) {
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();

            if (!response.isSuccessful())
                throw new SendRequestException("Запрос выполнен неудачно");

            if (responseBody == null)
                throw new SendRequestException("Отсутствует тело отклика");

            return responseBody.string();

        } catch (IOException e) {
            throw new SendRequestException("Ошибка выполнения запроса");
        }
    }
}
