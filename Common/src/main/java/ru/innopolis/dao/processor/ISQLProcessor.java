package ru.innopolis.dao.processor;

import java.sql.ResultSet;
import java.util.List;

/**
 * Создано: Денис
 * Дата:  02.11.2016
 * Описание: Интерфейс процессора для выолнения запросов в БД
 */
public interface ISQLProcessor {

    /**
     * Удалить сущность
     * @param object Сущность
     * @param <T> Тип сущности
     * @return Количество записей, оказавшихся под воздействием команды
     * @throws Exception Что-то пошло не так
     */
    <T> int delete(T object) throws Exception;

    /**
     * Добавить сущность в БД
     * @param object Сущность
     * @param <T> Тип сущности
     * @return Количество записей, оказавшихся под воздействием команды
     * @throws Exception Что-то пошло не так
     */
    <T> int insert(T object) throws Exception;

    /**
     * Обновить сущность в БД
     * @param object Сущность
     * @param <T> Тип сущности
     * @return Количество записей, оказавшихся под воздействием команды
     * @throws Exception Что-то пошло не так
     */
    <T> int update(T object) throws Exception;

    /**
     * Получить все записи из БД удолетворяющие условию
     * @param clazz Тип сущности
     * @param whereClause Условие выборки
     * @param <T> Тип сущности
     * @return Список извлечённых записей
     * @throws Exception Что-то пошло не так
     */
    <T> List<T> simpleSelect(Class<T> clazz, String whereClause) throws Exception;

    /**
     * Получить все записи из БД удолетворяющие условию
     * @param clazz Тип сущности
     * @param whereClause Условие выборки
     * @param <T> Тип сущности
     * @param arg Массив аргументов для подстановки
     * @return Список извлечённых записей
     * @throws Exception Что-то пошло не так
     */
    <T> List<T> simpleSelect(Class<T> clazz, String whereClause, Object[] arg) throws Exception;

    /**
     * Выполнить запрос в БД
     * @param action Действие
     * @param <T> Тип возвращаемого значения
     * @return Результат
     * @throws Exception Что-то пошло не так
     */
    <T> T execute(SQLAction<T> action) throws Exception;

    /**
     * Десериализовать ответ из БД
     * @param resultSet Данные из БД
     * @param clazz Класс, описывающий сущность, пришедшей из БД
     * @param <T> Тип сущности, пришедшей из БД
     * @return Список сущностей, пришедших из БД
     */
    <T> List<T> deserializeResponse(ResultSet resultSet, Class<T> clazz) throws Exception;

    /**
     * Выполнить произвольную команду на выборку
     * @param clazz Класс, который представляет результат выборки
     * @param query Запрос
     * @param arg Аргументы для запроса
     * @param <T> Тип результата
     * @return Список сущностей указанного типа
     * @throws Exception Что-то пошло не так
     */
    <T> List<T> executeSelect(Class<T> clazz, String query, Object[] arg) throws Exception;

    /**
     * Получить сущность по ИД
     * @param clazz Класс сущности, который представляет результат выборки
     * @param id ИД записи
     * @param <T> Тип возвращаемого значения
     * @return Сущность, если найдена. В противном случае null
     */
    <T> T getByID(Class<T> clazz, Long id) throws Exception;
}
