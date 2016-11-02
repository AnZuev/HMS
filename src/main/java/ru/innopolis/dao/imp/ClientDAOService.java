package ru.innopolis.dao.imp;

import ru.innopolis.dao.IClientDAOService;
import ru.innopolis.dao.entity.Client;
import ru.innopolis.dao.processor.ISQLProcessor;
import ru.innopolis.models.NewClientModel;

import javax.sql.DataSource;

/**
 * Создано: Денис
 * Дата:  29.10.2016
 * Описание: DAO Сервис по работе с клиетом
 */
public class ClientDAOService implements IClientDAOService {

    private ISQLProcessor sqlProcessor;

    public ClientDAOService(ISQLProcessor sqlProcessor) {
        this.sqlProcessor = sqlProcessor;
    }

    public Client addNewClient(NewClientModel client) throws Exception {
        return null;
    }

    public Client findClient(String email, String password) throws Exception {
        return null;
    }
}
