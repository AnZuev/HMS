package ru.innopolis.dao.imp;

import ru.innopolis.dao.IClientDAOService;
import ru.innopolis.dao.entity.Client;
import ru.innopolis.models.NewClientModel;

import javax.sql.DataSource;

/**
 * Создано: Денис
 * Дата:  29.10.2016
 * Описание: DAO Сервис по работе с клиетом
 */
public class ClientDAOService implements IClientDAOService {

    private DataSource source;

    public ClientDAOService(DataSource source) {
        this.source = source;
    }

    public Client addNewClient(NewClientModel client) throws Exception {
        return null;
    }

    public Client findClient(String email, String password) throws Exception {
        return null;
    }
}
