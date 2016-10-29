package ru.innopolis.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Создано: Денис
 * Дата:  17.10.2016
 * Описание: Модель нового клиента
 */
public class NewClientModel {

    @NotNull
    @Size(min=1, max=50)
    private String firstName;

    @NotNull
    @Size(min=1, max=50)
    private String secondName;

    @Size(min=1, max=50)
    private String fatherName;

    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "^8\\d{10}")
    private String phoneNumber;

    @NotNull
    @Size(min=10, max=50)
    private String password;

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

    @JsonProperty("mail")
    public String getEmail() {
        return email;
    }
    @JsonProperty("mail")
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
