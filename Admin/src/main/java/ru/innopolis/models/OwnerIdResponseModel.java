package ru.innopolis.models;

/**
 * Создано: Денис
 * Дата:  13.11.2016
 * Описание: Модель ответа на запрос создания нового владельца
 */
public class OwnerIdResponseModel {
    private Long ownerID;

    public Long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Long ownerID) {
        this.ownerID = ownerID;
    }
}
