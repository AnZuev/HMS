package ru.innopolis.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Создано: Денис
 * Дата:  29.10.2016
 * Описание: Модель запроса на аутентификацию
 */
public class AuthorizationRequestModel {

    @NotNull
    @Email
    private String mail;

    @NotNull
    @Size(min=10, max=50)
    private String password;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
