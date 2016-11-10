package ru.innopolis.dao.imp;

import org.multylanguages.exeption.MetaMessageException;
import org.multylanguages.message.MetaMessage;
import ru.innopolis.dao.IRoomDAOService;
import ru.innopolis.dao.entity.Client;
import ru.innopolis.dao.entity.Order;
import ru.innopolis.dao.entity.OrderDescription;
import ru.innopolis.dao.entity.Room;
import ru.innopolis.dao.entity.addition.ExtendedRoom;
import ru.innopolis.dao.processor.ISQLProcessor;
import ru.innopolis.exceptions.UserErrorCode;
import ru.innopolis.exceptions.UserException;

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

//    SELECT * FROM ROOMS R
//    WHERE R.HOTEL_ID = :HOTEL_ID
//    AND R.ROOM_TYPE_ID = :ROOM_TYPE_ID
//    AND R.ID NOT IN (
//            SELECT ROOM_ID FROM ORDERS ORD
//            WHERE ORD.HOTEL_ID = :HOTEL_ID
//            AND :START_DATE < ORD.FINISH_DATE
//            AND :FINISH_DATE > ORD.START_DATE
    private static final String freeRoomSelect = "SELECT * FROM ROOMS R " +
        "JOIN ROOM_TYPES RT ON R.ROOM_TYPE_ID = RT.ID " +
        "WHERE R.HOTEL_ID = {0} " +
        "AND R.ROOM_TYPE_ID = {1} " +
        "AND R.ID NOT IN (" +
        "            SELECT ROOM_ID FROM ORDERS ORD " +
        "            WHERE ORD.HOTEL_ID = {0} " +
        "            AND {2} < ORD.FINISH_DATE " +
        "            AND {3} > ORD.START_DATE)";

//    SELECT ROOM_ID FROM ORDERS ORD
//    WHERE ORD.HOTEL_ID = :HOTEL_ID
//    AND :START_DATE < ORD.FINISH_DATE
//    AND :FINISH_DATE > ORD.START_DATE
    private static final String checkIsRoomFree = "SELECT * FROM ORDERS ORD " +
            "WHERE ORD.HOTEL_ID = {0} AND ORD.ROOM_ID = {1} AND {2} < ORD.FINISH_DATE AND {3} > ORD.START_DATE";


//    SELECT ORD.ID ID, ORD.START_DATE START_DATE, ORD.FINISH_DATE FINISH_DATE, ORD.COST COST, ORD.STATUS STATUS, HOT.NAME NAME, R.ROOM_NUMBER ROOM_NUMBER
//    FROM ORDERS ORD
//    JOIN HOTELS HOT ON ORD.HOTEL_ID = HOT.ID
//    JOIN ROOMS R ON ORD.ROOM_ID = R.ID
//    WHERE ORD.CLIENT_ID = :ID;
    private static final String SELECT_CLIENT_ORDERS = "SELECT ORD.ID ID, ORD.START_DATE START_DATE, ORD.FINISH_DATE FINISH_DATE, ORD.COST COST, ORD.STATUS STATUS, HOT.NAME NAME, R.ROOM_NUMBER ROOM_NUMBER\n" +
        "FROM ORDERS ORD " +
        "JOIN HOTELS HOT ON ORD.HOTEL_ID = HOT.ID " +
        "JOIN ROOMS R ON ORD.ROOM_ID = R.ID " +
        "WHERE ORD.CLIENT_ID = {0}";

    private ISQLProcessor sqlProcessor;

    public RoomDAOService(ISQLProcessor sqlProcessor) {
        this.sqlProcessor = sqlProcessor;
    }

    public List<ExtendedRoom> getFreeRoomsInHotel(long hotelId, long roomTypeId, Calendar from, Calendar to) throws Exception {
        Object[] args = {hotelId, roomTypeId, from, to};
        List<ExtendedRoom> list = sqlProcessor.executeSelect(ExtendedRoom.class, freeRoomSelect, args);
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

    public void cancelBook(Client client, long orderId) throws Exception {
        Order order = sqlProcessor.getByID(Order.class, orderId);
        if (order == null || !order.getClientId().equals(client.getId())){
            MetaMessage message = new MetaMessage("order.is.not.found");
            throw new UserException(message, UserErrorCode.NOT_FOUND);
        }

        String status = order.getStatus();
        if (!"BOOK".equals(status)){
            MetaMessage message = new MetaMessage("order.can.not.be.canceled");
            throw new UserException(message, UserErrorCode.BAD_PARAMETERS);
        }
        order.setStatus("CANCELED");
        sqlProcessor.update(order);
    }

    public List<OrderDescription> getOrdersByClient(Client client) throws Exception {
        return sqlProcessor.executeSelect(OrderDescription.class, SELECT_CLIENT_ORDERS, new Object[]{client.getId()});
    }
}
