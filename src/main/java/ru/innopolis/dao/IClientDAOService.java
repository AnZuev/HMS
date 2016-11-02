package ru.innopolis.dao;

import ru.innopolis.dao.entity.Client;

/**
 * Создано: Денис
 * Дата:  28.10.2016
 * Описание: Сервис по работе с клиентом для взаимодействия с БД
 */
public interface IClientDAOService {

    /**
     * Добавить нового клиента
     * @param client Клиент
     * @return Сохранённый клиент
     * @throws Exception Не удалось добавить клиента по каким-либо причинам
     */
    Client addNewClient(Client client) throws Exception;

    /**
     * Найти клиента по электронной почте и паролю
     * @param email Электронная почта
     * @param password Пароль
     * @return Клиент, если существет. Null в противном случае
     */
    Client findClient(String email, String password) throws Exception;

}
