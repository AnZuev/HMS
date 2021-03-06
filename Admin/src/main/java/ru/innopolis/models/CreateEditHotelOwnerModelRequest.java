package ru.innopolis.models;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.*;

/**
 * Создано: Денис
 * Дата:  06.11.2016
 * Описание: Модель запроса на добавление/обновление сотрудника отеля
 */
public class CreateEditHotelOwnerModelRequest {
    @Max(value = Long.MAX_VALUE)
    @Min(value = 0)
    private Long id;

    @NotNull
    @Size(min=1, max=50)
    private String firstName;

    @NotNull
    @Size(min=1, max=50)
    private String secondName;

    @Size(min=1, max=50)
    private String fatherName;

    @NotNull
    @Email
    private String email;

    @Size(min=10, max=50)
    private String password;

    @NotNull
    @Max(value = Long.MAX_VALUE)
    @Min(value = 0)
    private Long hotelId;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }
}
