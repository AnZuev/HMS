package ru.innopolis.dao;

import ru.innopolis.dao.entity.Hotel;
import ru.innopolis.dao.entity.RoomType;

import java.util.List;

/**
 * Создано: Денис
 * Дата:  30.10.2016
 * Описание: Сервис по работе с отелем
 */
public interface IHotelDAOService {

    /**
     * Получить список всех отелей в системе
     * @return Список отелей
     */
    List<Hotel> getAllHotels() throws Exception;
}
