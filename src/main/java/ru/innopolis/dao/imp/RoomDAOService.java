package ru.innopolis.dao.imp;

import org.multylanguages.exeption.MetaMessageException;
import org.multylanguages.message.MetaMessage;
import ru.innopolis.dao.IRoomDAOService;
import ru.innopolis.dao.entity.Client;
import ru.innopolis.dao.entity.Order;
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
 * Описание: Реализация сервиса по работе с комнатой, хранящейся в БД
 */
public class RoomDAOService implements IRoomDAOService {

    private static final String freeRoomSelect = "SELECT * FROM ROOMS R " +
            "WHERE R.HOTEL_ID = {0} AND R.ROOM_TYPE_ID = {1} AND R.ID NOT IN (" +
            "  SELECT ROOM_ID FROM ORDERS ORD" +
            "  WHERE ORD.HOTEL_ID = {0} AND {2} < ORD.FINISH_DATE)";

//    SELECT ROOM_ID FROM ORDERS ORD
//    WHERE ORD.HOTEL_ID = :HOTEL_ID AND ORD.ROOM_ID = :ROOM_ID AND(:START_DATE >= ORD.FINISH_DATE OR :FINISH_DATE <= ORD.START_DATE);
    private static final String checkIsRoomFree = "SELECT * FROM ORDERS ORD " +
            "WHERE ORD.HOTEL_ID = {0} AND ORD.ROOM_ID = {1} AND ({2} >= ORD.FINISH_DATE OR {3} <= ORD.START_DATE)";

    private ISQLProcessor sqlProcessor;

    public RoomDAOService(ISQLProcessor sqlProcessor) {
        this.sqlProcessor = sqlProcessor;
    }

    public List<Room> getFreeRoomsInHotel(long hotelId, long roomTypeId, Calendar from, Calendar to) throws Exception {
        Object[] args = {hotelId, roomTypeId, from};
        List<Room> list = sqlProcessor.executeSelect(Room.class, freeRoomSelect, args);
        return list;
    }

    public void bookRoom(Client client, long roomID, Calendar from, Calendar to) throws Exception {

        Room room = sqlProcessor.getByID(Room.class, roomID);

        if (room == null){
            MetaMessage message = new MetaMessage("room.does.not.exist");
            throw  new MetaMessageException(message);
        }

        Object[] args = {room.getHotelId(), room.getId(), from, to};
        List<Order> list = sqlProcessor.executeSelect(Order.class, checkIsRoomFree, args);

        if (!list.isEmpty()){
            MetaMessage message = new MetaMessage("room.is.not.free");
            throw  new MetaMessageException(message);
        }

        Order order = new Order();
        order.setClientId(client.getId());
        order.setRoomId(roomID);
        order.setHotelId(room.getHotelId());
        order.setStartDate(from);
        order.setFinishDate(to);
        order.setStatus("BOOK");

        sqlProcessor.insert(order);

    }
}
