package ru.innopolis.dao;

import ru.innopolis.dao.entity.Employee;

/**
 * Создано: Денис
 * Дата:  06.11.2016
 * Описание: Сервис по работе со сотрудниками гостиницы
 */
public interface IEmployeeDAOService{

    /**
     * Добавить или обновить текущего сотрудника. Если у сущности проставлен ИД, то произойдёт обновление. Иначе создание.
     * @param employee Новые данные по сотруднику
     * @throws Exception Что-то пошло не так
     * @throws ru.innopolis.exceptions.UserException Пользователь с такми email уже существет
     */
    void addOrUpdate(Employee employee) throws Exception;
}
