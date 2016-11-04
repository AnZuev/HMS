package ru.innopolis.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.innopolis.json.serializes.CalendarSerializer;

import java.util.Calendar;

/**
 * Создано: Денис
 * Дата:  04.11.2016
 * Описание: Ответ на запрос получения информации о заказах клиента
 */
public class OrderedRoomResponseModel {
    private Long id;
    private String hotelTitle;
    private Integer roomNumber;
    @JsonSerialize(using = CalendarSerializer.class)
    private Calendar from;
    @JsonSerialize(using = CalendarSerializer.class)
    private Calendar to;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double cost;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHotelTitle() {
        return hotelTitle;
    }

    public void setHotelTitle(String hotelTitle) {
        this.hotelTitle = hotelTitle;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

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

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
