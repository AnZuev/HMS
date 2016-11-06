package ru.innopolis.dao.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Создано: Денис
 * Дата:  30.10.2016
 * Описание: Тип комнаты
 */
@Table(name = "ROOM_TYPES")
public class RoomType {
    @Id
    @SequenceGenerator(name = "ROOM_TYPES_SEQ", sequenceName = "ROOM_ID_SEQ_GEN")
    @Column(name = "id")
    private Long id;
    @Column(name = "HOTEL_ID")
    private Long hotelId;
    @Column(name = "TYPE_NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "COST")
    private Double cost;
    @Column(name = "PATH_TO_PHOTO")
    private String photoPath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }
}
