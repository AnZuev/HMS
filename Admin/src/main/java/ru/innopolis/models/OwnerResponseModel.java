package ru.innopolis.models;

/**
 * Создано: Денис
 * Дата:  06.11.2016
 * Описание: Модель ответа на запрос получения списка владельцев
 */
public class OwnerResponseModel {
    private Long id;
    private String firstName;
    private String secondName;
    private String fatherName;
    private String mail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
