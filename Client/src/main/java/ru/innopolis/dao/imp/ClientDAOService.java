package ru.innopolis.dao.imp;

import org.multylanguages.exeption.MetaMessageException;
import org.multylanguages.message.MetaMessage;
import ru.innopolis.dao.IClientDAOService;
import ru.innopolis.dao.entity.Client;
import ru.innopolis.dao.processor.ISQLProcessor;
import ru.innopolis.models.NewClientModel;

import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.List;

/**
 * Создано: Денис
 * Дата:  29.10.2016
 * Описание: DAO Сервис по работе с клиетом
 */
public class ClientDAOService implements IClientDAOService {

    private ISQLProcessor sqlProcessor;

    private MessageFormat findClientCondition = new MessageFormat("email = ''{0}'' and HASH_PASSWORD = ''{1}''");
    private MessageFormat findByEmailCondition = new MessageFormat("email = ''{0}''");

    public ClientDAOService(ISQLProcessor sqlProcessor) {
        this.sqlProcessor = sqlProcessor;
    }

    public Client addNewClient(Client client) throws Exception {
        String mail = client.getMail();
        String where = findByEmailCondition.format(new Object[]{mail});
        List<Client> clients = sqlProcessor.simpleSelect(Client.class, where);
        if (clients.isEmpty()){
            sqlProcessor.insert(client);
        }else {
            MetaMessage metaMessage = new MetaMessage("email.is.not.free");
            throw new MetaMessageException(metaMessage);
        }
        return client;
    }

    public Client findClient(String email, String password) throws Exception {
        String where = findClientCondition.format(new Object[]{email, password});
        List<Client> clients = sqlProcessor.simpleSelect(Client.class, where);
        return clients.isEmpty() ? null : clients.get(0);
    }

    public void updateClientInformation(Client client) throws Exception {
        sqlProcessor.update(client);
    }
}
