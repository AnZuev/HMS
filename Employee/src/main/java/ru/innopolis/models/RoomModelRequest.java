package ru.innopolis.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Создано: Денис
 * Дата:  11.11.2016
 * Описание:
 */
public class RoomModelRequest {
    @Min(0)
    @Max(Long.MAX_VALUE)
    private Long id;

    @NotNull
    @Min(0)
    @Max(Long.MAX_VALUE)
    private Long roomNumber;

    @NotNull
    @Pattern(regexp = "(WORKED)||(NOT_WORKED)||(DELETED)")
    private String status;
    @NotNull
    @Min(0)
    @Max(Long.MAX_VALUE)
    private Long typeID;


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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTypeID() {
        return typeID;
    }

    public void setTypeID(Long typeID) {
        this.typeID = typeID;
    }
}
