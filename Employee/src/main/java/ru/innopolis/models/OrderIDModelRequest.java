package ru.innopolis.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Создано: Денис
 * Дата:  10.11.2016
 * Описание: Запрос содержащий ИД заказа
 */
public class OrderIDModelRequest {
    @NotNull
    @Min(value = 0)
    @Max(value = Long.MAX_VALUE)
    private Long orderId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
