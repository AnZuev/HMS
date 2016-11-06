package ru.innopolis.dao;

import ru.innopolis.dao.entity.Staff;

/**
 * Создано: Денис
 * Дата:  06.11.2016
 * Описание: Сервис по работе с нашими сотрудниками
 */
public interface IStaffDAOService {

    /**
     * Найти сотрудника
     * @param email Почта
     * @param hashPassword Хешированный пароль
     * @return Сотрудник, если есть. В противном случае null
     * @throws Exception Что-то пошло не так
     */
    Staff findStaff(String email, String hashPassword) throws Exception;
}
