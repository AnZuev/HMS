package ru.innopolis.dao.entity;

/**
 * Создано: Денис
 * Дата:  30.10.2016
 * Описание: Тип комнаты
 */
public class RoomType {
    private long id;
    private String name;
    private String description;
    private double cost;
    private String photoPath;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
