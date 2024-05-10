package ru.urfu.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.controller.constant.ButtonsText;
import ru.urfu.controller.constant.Menus;
import ru.urfu.utils.callback.MultipageMenuCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Компонент для преобразования кнопок в многостраничные меню
 */
@Component
public class MultiPageKeyboard {

    /**
     * Получить количество страниц, на которое разделяется countOfButtons кнопок
     *
     * @param countOfButtons количество кнопок распределяющихся в меню
     * @param rowsCount      количество строк выделяемых под многостраничную часть меню
     * @return количество страниц на которое может быть разбит список кнопок
     */
    public int getCountOfPages(int countOfButtons, int rowsCount) {
        int countOfUsefulButtonsInPage = rowsCount - 1;

        int countOfPages = countOfButtons / countOfUsefulButtonsInPage;
        if (countOfButtons % countOfUsefulButtonsInPage != 0)
            countOfPages++;
        return countOfPages;
    }

    /**
     * Получить страницу
     *
     * @param pageIndex индекс возвращаемой страницы
     * @param buttons   список кнопок распределяющихся в меню
     * @param menu      меню, в котором реализуется многостраничный вывод
     * @param rowsCount количество строк выделяемых под многостраничную часть меню
     * @return строки InlineKeyboardButton
     */
    public List<List<InlineKeyboardButton>> getPage(int pageIndex, List<InlineKeyboardButton> buttons, Menus menu, int rowsCount) {

        // расчёт количества страниц
        int countOfUsefulButtonsInPage = rowsCount - 1;
        int countOfPages = getCountOfPages(buttons.size(), rowsCount);

        // обработка некорректного номера страницы
        if (pageIndex > countOfPages)
            pageIndex = countOfPages - 1;

        // кнопки многостраничного меню
        List<List<InlineKeyboardButton>> rows = new ArrayList<>(countOfPages);

        // добавляем кнопки в страницу
        buttons.stream()
                .skip((long) pageIndex * countOfUsefulButtonsInPage)
                .limit(countOfUsefulButtonsInPage)
                .forEach(button -> rows.add(List.of(button)));

        // добавляем кнопки навигации
        List<InlineKeyboardButton> navigate = new ArrayList<>();
        if (pageIndex > 0) {
            MultipageMenuCallback callback = new MultipageMenuCallback(menu, pageIndex - 1);
            navigate.add(InlineKeyboardButton.builder()
                    .text(ButtonsText.PREVIOUS_PAGE.getText())
                    .callbackData(callback.getData())
                    .build());
        }
        if (pageIndex < countOfPages - 1) {
            MultipageMenuCallback callback = new MultipageMenuCallback(menu, pageIndex + 1);
            navigate.add(InlineKeyboardButton.builder()
                    .text(ButtonsText.NEXT_PAGE.getText())
                    .callbackData(callback.getData())
                    .build());
        }
        if (!navigate.isEmpty())
            rows.add(navigate);

        return rows;
    }
}
