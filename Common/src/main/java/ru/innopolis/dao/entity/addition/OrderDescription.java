package ru.innopolis.dao.entity.addition;

import javax.persistence.Column;
import java.util.Calendar;

/**
 * Создано: Денис
 * Дата:  04.11.2016
 * Описание: Описание заказа на комнату
 */
public class OrderDescription {
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String hotelTitle;
    @Column(name = "ROOM_NUMBER")
    private Integer roomNumber;
    @Column(name = "START_DATE")
    private Calendar from;
    @Column(name = "FINISH_DATE")
    private Calendar to;
    @Column(name = "COST")
    private Double cost;
    @Column(name = "STATUS")
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
