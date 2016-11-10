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

    /**
     * Получить типы комнат в отеле
     * @param id ИД отеля
     * @return Список типов комнат в отеле
     */
    List<RoomType> getRoomTypesByHotelId(long id) throws Exception;

    /**
     * Создать новый отель
     * @param hotel Новый отель
     * @throws Exception Не удалось создать новый отель
     */
    void createHotel(Hotel hotel) throws Exception;

    /**
     * Обновить информацию по отелю
     * @param hotel Новая информация по отелю
     * @throws Exception Не удалось создать новый отель
     */
    void updateHotel(Hotel hotel) throws Exception;
}
