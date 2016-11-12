package ru.innopolis.dao;

import ru.innopolis.dao.imp.*;
import ru.innopolis.dao.processor.ISQLProcessor;
import ru.innopolis.dao.processor.SQLProcessor;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Создано: Денис
 * Дата:  01.11.2016
 * Описание: Фабрика предназначена для инициализации сервиса реализацией {@link ISQLProcessor}
 */
public class DAOServiceFactory implements IDAOServiceFactory {

    private static final String DIRECTORY = "java:/comp/env";
    private static final String JDBC_HMS_NAME = "jdbc/HMS";

    private static final Map<String, Class> mapper = new HashMap<>();
    private static volatile DAOServiceFactory factory;

    static {
        mapper.put(IClientDAOService.class.getName(), ClientDAOService.class);
        mapper.put(IHotelDAOService.class.getName(), HotelDAOService.class);
        mapper.put(IRoomDAOService.class.getName(), RoomDAOService.class);
        mapper.put(IStaffDAOService.class.getName(), StaffDAOService.class);
        mapper.put(IEmployeeDAOService.class.getName(), EmployeeDAOService.class);
    }

    private ISQLProcessor processor;

    private DAOServiceFactory(DataSource source) {
        processor = new SQLProcessor(source);
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
        Class<? extends T> impl = mapper.get(service.getName());
        Constructor<? extends T> constructor = impl.getConstructor(ISQLProcessor.class);
        return constructor.newInstance(processor);
    }
}
