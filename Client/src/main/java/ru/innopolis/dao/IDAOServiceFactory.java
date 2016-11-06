package ru.innopolis.dao;

/**
 * Создано: Денис
 * Дата:  01.11.2016
 * Описание: Интерфейс фабрики для создания DAO сервисов
 */
public interface IDAOServiceFactory {

    /**
     * Создать экземпляр DAO сервиса
     * @param service Сервис
     * @return Экземпляр сервиса
     * @throws Exception Неудалось создать сервис
     */
    <T> T createService(Class<T> service) throws Exception;
}
