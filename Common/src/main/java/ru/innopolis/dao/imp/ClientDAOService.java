package ru.innopolis.dao.imp;

import org.multylanguages.message.MetaMessage;
import ru.innopolis.dao.IClientDAOService;
import ru.innopolis.dao.entity.Client;
import ru.innopolis.dao.processor.ISQLProcessor;
import ru.innopolis.exceptions.UserErrorCode;
import ru.innopolis.exceptions.UserException;

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
    private static final String SEARCH_BY_EMAIL_CONDITION = "EMAIL = {0}";
    private static final MetaMessage EMAIL_IS_NOT_FREE_MESSAGE = new MetaMessage("email.is.not.free");

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
            throw new UserException(EMAIL_IS_NOT_FREE_MESSAGE, UserErrorCode.BAD_PARAMETERS);
        }
        return client;
    }

    public Client findClient(String email, String password) throws Exception {
        String where = findClientCondition.format(new Object[]{email, password});
        List<Client> clients = sqlProcessor.simpleSelect(Client.class, where);
        return clients.isEmpty() ? null : clients.get(0);
    }

    public void updateClientInformation(Client client) throws Exception {
        Client clientDBVersion = sqlProcessor.getByID(Client.class, client.getId());
        if (!clientDBVersion.getMail().equals(client.getMail())){
            List<Client> clients = sqlProcessor.simpleSelect(Client.class, SEARCH_BY_EMAIL_CONDITION, new Object[]{client.getMail()});
            if (!clients.isEmpty()){
                throw new UserException(EMAIL_IS_NOT_FREE_MESSAGE, UserErrorCode.BAD_PARAMETERS);
            }
        }
        sqlProcessor.update(client);
    }
}
