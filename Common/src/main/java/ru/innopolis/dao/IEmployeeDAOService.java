package ru.innopolis.dao;

import ru.innopolis.dao.entity.Employee;

import java.util.List;

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

    /**
     * Получить список владельцев отелей
     * @return Список владельцев отелей
     * @throws Exception Что-то пошло не так
     */
    List<Employee> getOwners() throws Exception;

    /**
     * Найти сотрудника отеля
     * @param email Почта
     * @param hashPassword Хешированный пароль
     * @return Сотрудник, если есть. В противном случае null
     * @throws Exception Что-то пошло не так
     */
    Employee findEmployee(String email, String hashPassword) throws Exception;

    /**
     * Получить всех менеджеров в данном отеле
     * @param hotelId ИД отеля
     * @return Список работающих менеджеров
     * @throws Exception Что-то пошло не так
     */
    List<Employee> getManagersByHotelId(long hotelId) throws Exception;

    /**
     * Удалить сотрудника
     * @param employee Сотрудник
     * @throws Exception Не смогли удалить или была попытка удалить то, что нельзя.
     */
    void delete(Employee employee) throws Exception;
}
