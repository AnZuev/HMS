package ru.innopolis.dao.imp;

import ru.innopolis.dao.IRoomDAOService;
import ru.innopolis.dao.entity.Room;

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

    public List<Room> getFreeRoomsInHotel(long hotelId, long roomTypeId, Calendar from, Calendar to) throws Exception {
       return Collections.emptyList();
    }
}
