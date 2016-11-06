package ru.innopolis.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.Calendar;

/**
 * Создано: Денис
 * Дата:  01.11.2016
 * Описание: Модель запроса на поиск доступных комнат
 */
public class AvailableRoomRequestModel {
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Future
    private Calendar from;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Future
    private Calendar to;

    @NotNull
    @Max(value = Long.MAX_VALUE)
    @Min(value = 0)
    private long roomTypeId;

    @NotNull
    @Max(value = Long.MAX_VALUE)
    @Min(value = 0)
    private long hotelId;

    public Calendar getFrom() {
        return from;
    }

    public void setFrom(Calendar from) {
        this.from = from;
    }

    public Calendar getTo() {
        return to;
    }

    public void setTo(Calendar to) {
        this.to = to;
    }

    public long getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(long roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }
}
