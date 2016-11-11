package ru.innopolis.dao.entity.addition;

import ru.innopolis.dao.entity.OrderStatus;

import javax.persistence.Column;
import java.util.Calendar;

/**
 * Создано: Денис
 * Дата:  11.11.2016
 * Описание: Описание заказа для менеджера отеря
 */
public class ManagerOrderDescription {
    @Column(name = "ID")
    private Long id;
    @Column(name = "ROOM_NUMBER")
    private Integer roomNumber;
    @Column(name = "START_DATE")
    private Calendar startDate;
    @Column(name = "FINISH_DATE")
    private Calendar finishDate;
    @Column(name = "COST")
    private Double cost;
    @Column(name = "STATUS")
    private OrderStatus status;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "SECOND_NAME")
    private String secondName;
    @Column(name = "FATHER_NAME")
    private String fatherName;
    @Column(name = "PHONE_NUMBER")
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

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
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
