package ru.urfu.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.urfu.controller.menu.constant.ButtonsText;
import ru.urfu.controller.menu.constant.Flags;

import java.util.ArrayList;
import java.util.List;

/**
 * Компонент для преобразования кнопок в многостраничные меню
 */
@Component
public class MultiPageKeyboard {

    private final static String NEXT_PAGE_BUTTON_TEXT = ButtonsText.NEXT_PAGE.getText();
    private final static String PREVIOUS_PAGE_BUTTON_TEXT = ButtonsText.PREVIOUS_PAGE.getText();
    private final static String PAGE_INDEX_FLAG = Flags.PAGE_INDEX_FLAG.getName();

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
     * @param pageIndex    индекс возвращаемой страницы
     * @param buttons      список кнопок распределяющихся в меню
     * @param menuCallback callback который вызывает открытие данного меню
     * @param rowsCount    количество строк выделяемых под многостраничную часть меню
     * @return строки InlineKeyboardButton
     */
    public List<List<InlineKeyboardButton>> getPage(int pageIndex, List<InlineKeyboardButton> buttons, String menuCallback, int rowsCount) {

        // расчёт количества страниц
        int countOfUsefulButtonsInPage = rowsCount - 1;
        int countOfPages = getCountOfPages(buttons.size(), rowsCount);

        // обработка некоректного номера страницы
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
            Callback callback = new Callback(menuCallback);
            callback.addFlag(PAGE_INDEX_FLAG, String.valueOf(pageIndex - 1));
            navigate.add(InlineKeyboardButton.builder()
                    .text(PREVIOUS_PAGE_BUTTON_TEXT)
                    .callbackData(callback.toString())
                    .build());
        }
        if (pageIndex < countOfPages - 1) {
            Callback callback = new Callback(menuCallback);
            callback.addFlag(PAGE_INDEX_FLAG, String.valueOf(pageIndex + 1));
            navigate.add(InlineKeyboardButton.builder()
                    .text(NEXT_PAGE_BUTTON_TEXT)
                    .callbackData(callback.toString())
                    .build());
        }
        rows.add(navigate);

        return rows;
    }
}
