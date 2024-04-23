package ru.urfu.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Компонент для преобразования кнопок в многостраничные меню
 */
@Component
public class MultiPageKeyboard {

    @Autowired
    private CallbackFlagManager callbackFlagManager;

    /**
     * @param buttons список кнопок распределяющихся в меню
     * @param rowsCount количество строк выделяемых под многостраничную часть меню
     * @return количество страниц на которое может быть разбит список кнопок
     */
    public int getCountOfPages(List<InlineKeyboardButton> buttons, int rowsCount) {
        int countOfUsefulButtonsInPage = rowsCount - 1;

        // расчёт количества страниц
        int countOfPages = buttons.size() / countOfUsefulButtonsInPage;
        if (buttons.size() % countOfUsefulButtonsInPage != 0)
            countOfPages++;
        return countOfPages;
    }

    /**
     * @param pageIndex индекс возвращаемой страницы
     * @param buttons список кнопок распределяющихся в меню
     * @param menuCallback callback который вызывает открытие данного меню
     * @param rowsCount количество строк выделяемых под многостраничную часть меню
     * @return строки InlineKeyboardButton
     */
    public List<List<InlineKeyboardButton>> getPage(int pageIndex, List<InlineKeyboardButton> buttons, String menuCallback, int rowsCount) {

        // расчёт количества страниц
        int countOfUsefulButtonsInPage = rowsCount - 1;
        int countOfPages = getCountOfPages(buttons, rowsCount);

        // обработка некоректного номера страницы
        if (pageIndex > countOfPages)
            pageIndex = countOfPages - 1;


        List<List<InlineKeyboardButton>> rows = new ArrayList<>(countOfPages);

        // добавляем кнопки в страницу
        for (int i = pageIndex * countOfUsefulButtonsInPage; i < (pageIndex + 1) * countOfUsefulButtonsInPage; i++) {
            if (i == buttons.size())
                break;
            rows.add(List.of(buttons.get(i)));
        }

        // добавляем кнопки навигации9
        List<InlineKeyboardButton> navigate = new ArrayList<>();
        if (pageIndex > 0) {
            navigate.add(InlineKeyboardButton.builder()
                    .text("пред. стр.")
                    .callbackData(callbackFlagManager.setFlag(menuCallback, String.valueOf(pageIndex - 1)))
                    .build());
        }
        if (pageIndex < countOfPages - 1) {
            navigate.add(InlineKeyboardButton.builder()
                    .text("сл. стр.")
                    .callbackData(callbackFlagManager.setFlag(menuCallback, String.valueOf(pageIndex + 1)))
                    .build());
        }
        rows.add(navigate);

        return rows;
    }
}
