package ru.innopolis.models;


/**
 * Создано: Денис
 * Дата:  30.10.2016
 * Описание: Модель ответа на запрос аутентификации
 */
public class AuthorizationResponseModel {

    private Long id;
    private String firstName;
    private String secondName;
    private String fatherName;
    private String employeeType;

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }
}
