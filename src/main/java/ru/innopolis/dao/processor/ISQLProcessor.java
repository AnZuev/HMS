package ru.innopolis.dao.processor;

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
     * Выполнить запрос в БД
     * @param action Действие
     * @param <T> Тип возвращаемого значения
     * @return Результат
     * @throws Exception Что-то пошло не так
     */
    <T> T execute(SQLAction<T> action) throws Exception;
}
