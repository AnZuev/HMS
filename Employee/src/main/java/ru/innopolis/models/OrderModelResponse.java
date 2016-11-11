package ru.innopolis.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.innopolis.json.serializes.CalendarSerializer;

import java.util.Calendar;

/**
 * Создано: Денис
 * Дата:  10.11.2016
 * Описание: Подробное описание брони для менеджера
 */
public class OrderModelResponse {
    private Long id;
    private Integer roomNumber;
    @JsonSerialize(using = CalendarSerializer.class)
    private Calendar startDate;
    @JsonSerialize(using = CalendarSerializer.class)
    private Calendar finishDate;
    private String status;
    private Double cost;
    private String firstName;
    private String secondName;
    private String fatherName;
    private String phoneNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Calendar finishDate) {
        this.finishDate = finishDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
