package ru.innopolis.dao.imp;

import ru.innopolis.dao.IHotelDAOService;
import ru.innopolis.dao.entity.Hotel;
import ru.innopolis.dao.entity.RoomType;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

/**
 * Создано: Денис
 * Дата:  30.10.2016
 * Описание: Сервис по работе с отелем
 */
public class HotelDAOService implements IHotelDAOService {

    private DataSource source;

    public HotelDAOService(DataSource source) {
        this.source = source;
    }

    public List<Hotel> getAllHotels() throws Exception {
        return Collections.emptyList();
    }

    public List<RoomType> getRoomTypesByHotelId(long id) throws Exception {
        return Collections.emptyList();
    }
}
