package ru.innopolis.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Calendar;

/**
 * Создано: Денис
 * Дата:  03.11.2016
 * Описание: Запрос на бронь отеля
 */
public class BookModelRequest {
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
    private Long roomId;

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

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}
