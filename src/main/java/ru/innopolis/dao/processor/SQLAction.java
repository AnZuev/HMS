package ru.innopolis.dao.processor;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Создано: Денис
 * Дата:  02.11.2016
 * Описание: SQL действие.
 */
@FunctionalInterface
public interface SQLAction<T> {

    /**
     * Выполнить действие
     * @param s Оператор
     * @return Результат выполнения
     * @throws SQLException Что-то пошло не так
     */
    T execute(Statement s) throws SQLException;
}
