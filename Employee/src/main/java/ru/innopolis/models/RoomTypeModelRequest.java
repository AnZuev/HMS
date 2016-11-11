package ru.innopolis.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Создано: Денис
 * Дата:  11.11.2016
 * Описание: Запрос на создание/обновление сущности "Тип комнаты"
 */
public class RoomTypeModelRequest {
    @Min(0)
    @Max(Long.MAX_VALUE)
    private Long id;
    @NotNull
    @Size(min = 3, max = 100)
    private String name;
    @NotNull
    @Size(min = 10, max = 500)
    private String description;
    @NotNull
    @Min(0)
    private Double cost;
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
}
