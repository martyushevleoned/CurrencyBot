package ru.urfu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.ApiManager;
import ru.urfu.model.Request;
import ru.urfu.model.Response;

import java.util.Comparator;
import java.util.List;

/**
 * Класс для взаимодействия с модулем currency-api
 */
@Service
public class ApiService {

    private final ApiManager apiManager = new ApiManager();

    @Autowired
    private DataConverterService dataConverter;

    /**
     * Возвращает все доступные запросы к API в видде списка кнопок
     */
    public List<InlineKeyboardButton> getRequestButtons() {
        return apiManager.getPossibleRequests().stream()
                .map(dataConverter::requestToInlineKeyboardButton)
                .sorted(Comparator.comparing(InlineKeyboardButton::getText))
                .toList();
    }

    /**
     * Возвращает стоимость валюты из модуля currency-api
     */
    public Response getPrice(Request request) {
        return apiManager.getPrice(request);
    }
}
