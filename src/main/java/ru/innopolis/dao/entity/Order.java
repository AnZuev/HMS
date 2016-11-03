package ru.innopolis.dao.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Calendar;

/**
 * Создано: Денис
 * Дата:  03.11.2016
 * Описание: Описание заказа
 */
@Table(name = "ORDERS")
public class Order {
    @Id
    @SequenceGenerator(name = "ORDERS_SEQ")
    @Column(name = "ID")
    private Long id;
    @Column(name = "CLIENT_ID")
    private Long clientId;
    @Column(name = "HOTEL_ID")
    private Long hotelId;
    @Column(name = "ROOM_ID")
    private Long roomId;
    @Column(name = "START_DATE")
    private Calendar startDate;
    @Column(name = "FINISH_DATE")
    private Calendar finishDate;
    @Column(name = "STATUS")
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

