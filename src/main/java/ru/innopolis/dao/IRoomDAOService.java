package ru.innopolis.dao;

import ru.innopolis.dao.entity.Room;

import java.util.Calendar;
import java.util.List;

/**
 * Создано: Денис
 * Дата:  01.11.2016
 * Описание: Сервис по работе с сущностью комната
 */
public interface IRoomDAOService {

    /**
     * Получить список доступных комнат
     * @param hotelId Ид отеля
     * @param roomTypeId Ид типа доступной комнаты
     * @param from Дата заезда
     * @param to Дата выезда
     * @return Список свободных комнат
     */
    List<Room> getFreeRoomsInHotel(long hotelId, long roomTypeId, Calendar from, Calendar to) throws Exception;
}
