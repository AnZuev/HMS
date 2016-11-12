package ru.innopolis.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Создано: Денис
 * Дата:  07.11.2016
 * Описание: Модель запроса на редактирование отеля
 */
public class EditHotelModelRequest {
    @NotNull
    @Size(min = 1)
    @JsonProperty("title")
    private String name;
    @NotNull
    @Size(min = 10)
    private String description;
    @NotNull
    @Pattern(regexp = "^8\\d{10}")
    private String phoneNumber;
    @NotNull
    @Size(min = 1)
    private String address;
    @NotNull
    @Email
    private String mail;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
