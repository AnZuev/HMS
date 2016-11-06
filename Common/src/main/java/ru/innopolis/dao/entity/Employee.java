package ru.innopolis.dao.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Создано: Денис
 * Дата:  06.11.2016
 * Описание: Сотрудник гостиницы
 */
@Table(name = "EMPLOYEES")
public class Employee {
    public enum Type{
        OWNER,
        MANAGER
    }

    @Id
    @SequenceGenerator(name = "EMPLOYEES_SEQ")
    @Column(name = "ID")
    private Long id;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "SECOND_NAME")
    private String secondName;
    @Column(name = "FATHER_NAME")
    private String fatherName;
    @Column(name = "EMAIL")
    private String mail;
    @Column(name = "HASH_PASSWORD")
    private String password;
    @Column(name = "TYPE")
    private Type type;
    @Column(name = "HOTEL_ID")
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }
}
