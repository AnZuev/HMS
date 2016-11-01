package ru.innopolis.dao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.lang.reflect.Constructor;

/**
 * Создано: Денис
 * Дата:  01.11.2016
 * Описание: Фабрика предназначена для инициализации сервиса экземпляром {@link DataSource}
 */
public class DAOServiceFactory implements IDAOServiceFactory {

    private static final String DIRECTORY = "java:/comp/env";
    private static final String JDBC_HMS_NAME = "jdbc/HMS";
    private static volatile DAOServiceFactory factory;
    private DataSource dataSource;

    private DAOServiceFactory(DataSource source) {
        dataSource = source;
    }

    public static DAOServiceFactory getInstance() throws Exception {
        if (factory == null) {
            synchronized (DAOServiceFactory.class) {
                if (factory == null) {
                    Context initContext = new InitialContext();
                    Context envContext = (Context) initContext.lookup(DIRECTORY);
                    DataSource dataSource = (DataSource) envContext.lookup(JDBC_HMS_NAME);
                    factory = new DAOServiceFactory(dataSource);
                }
            }
        }
        return factory;
    }

    public <T> T createService(Class<T> service) throws Exception {
        Constructor<T> constructor = service.getConstructor(DataSource.class);
        return constructor.newInstance(dataSource);
    }
}
