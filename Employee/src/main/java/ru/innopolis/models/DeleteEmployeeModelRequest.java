package ru.innopolis.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Создано: Денис
 * Дата:  10.11.2016
 * Описание: Модель запроса на удаление сотрудника
 */
public class DeleteEmployeeModelRequest {
    @NotNull
    @Min(value = 0)
    @Max(value = Long.MAX_VALUE)
    private Long employeeId;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
