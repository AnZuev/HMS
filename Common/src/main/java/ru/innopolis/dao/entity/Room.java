package ru.innopolis.dao.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Создано: Денис
 * Дата:  01.11.2016
 * Описание: Сущность "комната"
 */
@Table(name = "ROOMS")
public class Room {
    @Id
    @SequenceGenerator(name = "ROOMS_SEQ")
    @Column(name = "ID")
    private Long id;
    @Column(name = "ROOM_NUMBER")
    private Long roomNumber;
    @Column(name = "HOTEL_ID")
    private Long hotelId;
    @Column(name = "ROOM_TYPE_ID")
    private Long type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Long roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }
}
