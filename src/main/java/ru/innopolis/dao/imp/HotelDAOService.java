package ru.innopolis.dao.imp;

import ru.innopolis.dao.IHotelDAOService;
import ru.innopolis.dao.entity.Hotel;
import ru.innopolis.dao.entity.RoomType;
import ru.innopolis.dao.processor.ISQLProcessor;
import ru.innopolis.dao.processor.SQLProcessor;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

/**
 * Создано: Денис
 * Дата:  30.10.2016
 * Описание: Сервис по работе с отелем
 */
public class HotelDAOService implements IHotelDAOService {

    private ISQLProcessor sqlProcessor;

    public HotelDAOService(ISQLProcessor sqlProcessor) {
        this.sqlProcessor = sqlProcessor;
    }

    public List<Hotel> getAllHotels() throws Exception {
        return sqlProcessor.simpleSelect(Hotel.class, null);
    }

    public List<RoomType> getRoomTypesByHotelId(long id) throws Exception {
        return Collections.emptyList();
    }
}
