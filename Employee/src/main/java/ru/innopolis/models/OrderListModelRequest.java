package ru.innopolis.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Calendar;

/**
 * Создано: Денис
 * Дата:  10.11.2016
 * Описание: Запрос на получения списка заказов
 */
public class OrderListModelRequest {
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Calendar startDate;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Calendar finishDate;

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Calendar finishDate) {
        this.finishDate = finishDate;
    }
}
