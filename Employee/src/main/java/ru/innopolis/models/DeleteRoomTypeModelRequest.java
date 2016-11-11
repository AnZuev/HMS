package ru.innopolis.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Создано: Денис
 * Дата:  11.11.2016
 * Описание: Модель запроса на удаление сущности "Тип комнаты"
 */
public class DeleteRoomTypeModelRequest {
    @NotNull
    @Min(0)
    @Max(Long.MAX_VALUE)
    private Long roomTypeId;

    public Long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }
}
