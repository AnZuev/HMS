package ru.innopolis.dao.imp;

import org.multylanguages.message.MetaMessage;
import ru.innopolis.dao.IHotelDAOService;
import ru.innopolis.dao.entity.Hotel;
import ru.innopolis.dao.entity.RoomType;
import ru.innopolis.dao.entity.addition.RoomTypeStatus;
import ru.innopolis.dao.processor.ISQLProcessor;
import ru.innopolis.exceptions.UserErrorCode;
import ru.innopolis.exceptions.UserException;
import java.util.List;

/**
 * Создано: Денис
 * Дата:  30.10.2016
 * Описание: Сервис по работе с отелем
 */
public class HotelDAOService implements IHotelDAOService {

    private static final MetaMessage ROOM_TYPE_DOES_NOT_EXIST_MESSAGE = new MetaMessage("room.type.does.not.exist");
    private ISQLProcessor sqlProcessor;

    public HotelDAOService(ISQLProcessor sqlProcessor) {
        this.sqlProcessor = sqlProcessor;
    }

    public List<Hotel> getAllHotels() throws Exception {
        return sqlProcessor.simpleSelect(Hotel.class, null);
    }

    public List<RoomType> getRoomTypesByHotelId(long id) throws Exception {
        String where = "STATUS <> 'DELETED' AND HOTEL_ID = " + id;
        return sqlProcessor.simpleSelect(RoomType.class, where);
    }

    public void createHotel(Hotel hotel) throws Exception {
        sqlProcessor.insert(hotel);
    }

    public void updateHotel(Hotel hotel) throws Exception {
        sqlProcessor.update(hotel);
    }

    public void createOrUpdateRoomType(RoomType type) throws Exception {
        if (type.getId() == null){
            sqlProcessor.insert(type);
        }else {
            RoomType rtDB = sqlProcessor.getByID(RoomType.class, type.getId());
            if (rtDB == null || !rtDB.getHotelId().equals(type.getHotelId())){
                throw new UserException(ROOM_TYPE_DOES_NOT_EXIST_MESSAGE, UserErrorCode.BAD_PARAMETERS);
            }
            sqlProcessor.update(type);
        }
    }

    public void deleteRoomType(long roomTypeId, long hotelId) throws Exception {
        RoomType roomType = sqlProcessor.getByID(RoomType.class, roomTypeId);
        if (roomType == null || !roomType.getHotelId().equals(hotelId) || roomType.getStatus() == RoomTypeStatus.DELETED){
            throw new UserException(ROOM_TYPE_DOES_NOT_EXIST_MESSAGE, UserErrorCode.BAD_PARAMETERS);
        }
        roomType.setStatus(RoomTypeStatus.DELETED);
        sqlProcessor.update(roomType);
    }
}
