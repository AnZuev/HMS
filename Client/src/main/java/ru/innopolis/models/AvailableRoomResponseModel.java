package ru.innopolis.models;

/**
 * Создано: Денис
 * Дата:  01.11.2016
 * Описание: Модель ответа, описывающая доступную комнату
 */
public class AvailableRoomResponseModel {

    private long id;
    private long roomNumber;

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
}
