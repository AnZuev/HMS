package ru.innopolis.dao.entity;

/**
 * Создано: Денис
 * Дата:  01.11.2016
 * Описание: Сущность "комната"
 */
public class Room {
    private long id;
    private long roomNumber;
    private long hotelId;
    private RoomType type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(long roomNumber) {
        this.roomNumber = roomNumber;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }
}
