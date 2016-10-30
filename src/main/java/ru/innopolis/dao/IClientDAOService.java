package ru.innopolis.dao;

import ru.innopolis.dao.entity.Client;
import ru.innopolis.models.NewClientModel;

/**
 * Создано: Денис
 * Дата:  28.10.2016
 * Описание: Сервис по работе с клиентом для взаимодействия с БД
 */
public interface IClientDAOService {

    /**
     * Добавить нового клиента
     * @param client Клиент
     * @throws Exception Не удалось добавить клиента по каким-либо причинам
     */
    void addNewClient(NewClientModel client) throws Exception;

    /**
     * Найти клиента по электронной почте и паролю
     * @param email Электронная почта
     * @param password Пароль
     * @return Клиент, если существет. Null в противном случае
     */
    Client findClient(String email, String password) throws Exception;

}
