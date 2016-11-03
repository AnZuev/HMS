package ru.innopolis.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Создано: Денис
 * Дата:  03.11.2016
 * Описание: Модель описывает запрос на отмену заказа
 */
public class CancelBookRequestModel {

    @NotNull
    @Max(value = Long.MAX_VALUE)
    @Min(value = 0)
    private long orderId;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
