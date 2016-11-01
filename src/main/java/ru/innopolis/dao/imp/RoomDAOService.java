package ru.innopolis.dao.imp;

import ru.innopolis.dao.IRoomDAOService;
import ru.innopolis.dao.entity.Room;

import javax.sql.DataSource;
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

    private DataSource source;

    public RoomDAOService(DataSource source) {
        this.source = source;
    }

    public List<Room> getFreeRoomsInHotel(long hotelId, long roomTypeId, Calendar from, Calendar to) throws Exception {
       return Collections.emptyList();
    }
}
