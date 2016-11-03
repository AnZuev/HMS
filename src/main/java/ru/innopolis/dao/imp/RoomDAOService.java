package ru.innopolis.dao.imp;

import ru.innopolis.dao.IRoomDAOService;
import ru.innopolis.dao.entity.Room;
import ru.innopolis.dao.processor.ISQLProcessor;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Создано: Денис
 * Дата:  01.11.2016
 * Описание: Реализация сервиса по работе в комнатой, хранящейся в БД
 */
public class RoomDAOService implements IRoomDAOService {

    private static final String freeRoomSelect = "SELECT * FROM ROOMS R " +
            "WHERE R.HOTEL_ID = {0} AND R.ROOM_TYPE_ID = {1} AND R.ID NOT IN (" +
            "  SELECT ROOM_ID FROM ORDERS ORD" +
            "  WHERE ORD.HOTEL_ID = {0} AND {2} < ORD.FINISH_DATE)";

    private ISQLProcessor sqlProcessor;

    public RoomDAOService(ISQLProcessor sqlProcessor) {
        this.sqlProcessor = sqlProcessor;
    }

    public List<Room> getFreeRoomsInHotel(long hotelId, long roomTypeId, Calendar from, Calendar to) throws Exception {
        Object[] args = {hotelId, roomTypeId, from};
        List<Room> list = sqlProcessor.executeSelect(Room.class, freeRoomSelect, args);
        return list;
    }
}
