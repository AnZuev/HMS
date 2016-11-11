package ru.innopolis.dao.imp;

import org.multylanguages.exeption.MetaMessageException;
import org.multylanguages.message.MetaMessage;
import ru.innopolis.dao.IRoomDAOService;
import ru.innopolis.dao.entity.*;
import ru.innopolis.dao.entity.addition.ExtendedRoom;
import ru.innopolis.dao.entity.addition.ManagerOrderDescription;
import ru.innopolis.dao.entity.addition.RoomTypeStatus;
import ru.innopolis.dao.processor.ISQLProcessor;
import ru.innopolis.exceptions.UserErrorCode;
import ru.innopolis.exceptions.UserException;

import java.util.Calendar;
import java.util.List;

/**
 * Создано: Денис
 * Дата:  01.11.2016
 * Описание: Реализация сервиса по работе с комнатой, хранящейся в БД
 */
public class RoomDAOService implements IRoomDAOService {
    private static final MetaMessage ORDER_IS_NOT_FOUND_MESSAGE = new MetaMessage("order.is.not.found");
    private static final MetaMessage ORDER_IS_PAID_MESSAGE = new MetaMessage("order.is.paid");
    private static final MetaMessage ORDER_IS_CANCELED_MESSAGE = new MetaMessage("order.is.canceled");
    private static final MetaMessage ROOM_DOES_NOT_EXIST_MESSAGE = new MetaMessage("room.does.not.exist");
    private static final MetaMessage ROOM_TYPE_DOES_NOT_EXIST_MESSAGE = new MetaMessage("room.type.does.not.exist");
    private static final MetaMessage PAID_ORDER_CAN_NOT_BE_CANCELED_MESSAGE = new MetaMessage("paid.order.can.not.be.canceled");
    private static final MetaMessage CANCELED_ORDER_CAN_NOT_BE_CANCELED_MESSAGE = new MetaMessage("canceled.order.can.not.be.canceled");

//    SELECT * FROM ROOMS R
//    WHERE R.HOTEL_ID = :HOTEL_ID
//    AND R.ROOM_TYPE_ID = :ROOM_TYPE_ID
//    AND RT.STATUS <> 'DELETED'
//    AND R.ID NOT IN (
//            SELECT ROOM_ID FROM ORDERS ORD
//            WHERE ORD.HOTEL_ID = :HOTEL_ID
//            AND :START_DATE < ORD.FINISH_DATE
//            AND :FINISH_DATE > ORD.START_DATE
    private static final String freeRoomSelect = "SELECT * FROM ROOMS R " +
        "JOIN ROOM_TYPES RT ON R.ROOM_TYPE_ID = RT.ID " +
        "WHERE R.HOTEL_ID = {0} " +
        "AND R.ROOM_TYPE_ID = {1} " +
        "AND RT.STATUS <> ''DELETED''" +
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

//    SELECT ORD.ID ID, ORD.START_DATE START_DATE, ORD.FINISH_DATE FINISH_DATE, ORD.COST COST, ORD.STATUS STATUS, R.ROOM_NUMBER ROOM_NUMBER
//    FROM ORDERS ORD
//    JOIN ROOMS R ON ORD.ROOM_ID = R.ID
//    WHERE ORD.HOTEL_ID = :HOTEL_ID and :START_DATE <= START_DATE and FINISH_DATE <= :FINISH_DATE;

    private static final String SELECT_ORDERS_IN_HOTEL = "SELECT ORD.ID ID, ORD.START_DATE START_DATE, ORD.FINISH_DATE FINISH_DATE," +
            "ORD.COST COST, ORD.STATUS STATUS, R.ROOM_NUMBER ROOM_NUMBER, ORD.CLIENT_ID CLIENT_ID," +
            "CL.FIRST_NAME FIRST_NAME, CL.SECOND_NAME SECOND_NAME, CL.FATHER_NAME FATHER_NAME, CL.PHONE_NUMBER PHONE_NUMBER\n" +
            "FROM ORDERS ORD\n" +
            "JOIN ROOMS R ON ORD.ROOM_ID = R.ID\n" +
            "JOIN CLIENTS CL ON CL.ID = ORD.CLIENT_ID\n" +
            "WHERE ORD.HOTEL_ID = {0} and {1} <= START_DATE and FINISH_DATE <= {2}";

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
        order.setStatus(OrderStatus.BOOKED);

        sqlProcessor.insert(order);

    }

    public void cancelBook(Client client, long orderId) throws Exception {
        Order order = sqlProcessor.getByID(Order.class, orderId);
        if (order == null || !order.getClientId().equals(client.getId())){
            MetaMessage message = new MetaMessage("order.is.not.found");
            throw new UserException(message, UserErrorCode.NOT_FOUND);
        }

        if (order.getStatus() != OrderStatus.BOOKED){
            MetaMessage message = new MetaMessage("order.can.not.be.canceled");
            throw new UserException(message, UserErrorCode.BAD_PARAMETERS);
        }
        order.setStatus(OrderStatus.CANCELED);
        sqlProcessor.update(order);
    }

    public List<OrderDescription> getOrdersByClient(Client client) throws Exception {
        return sqlProcessor.executeSelect(OrderDescription.class, SELECT_CLIENT_ORDERS, new Object[]{client.getId()});
    }

    public void payRoom(long orderID, long hotelID) throws Exception {
        Order order = sqlProcessor.getByID(Order.class, orderID);
        if (order == null || hotelID != order.getHotelId()){
            throw new UserException(ORDER_IS_NOT_FOUND_MESSAGE);
        }
        if (order.getStatus() == OrderStatus.PAYED){
            throw new UserException(ORDER_IS_PAID_MESSAGE);
        }
        if (order.getStatus() == OrderStatus.CANCELED){
            throw new UserException(ORDER_IS_CANCELED_MESSAGE);
        }
        order.setStatus(OrderStatus.PAYED);
        sqlProcessor.update(order);
    }

    public void cancelOrder(long orderID, long hotelID) throws Exception {
        Order order = sqlProcessor.getByID(Order.class, orderID);
        if (order == null || hotelID != order.getHotelId()){
            throw new UserException(ORDER_IS_NOT_FOUND_MESSAGE);
        }
        if (order.getStatus() == OrderStatus.PAYED){
            throw new UserException(PAID_ORDER_CAN_NOT_BE_CANCELED_MESSAGE);
        }
        if (order.getStatus() == OrderStatus.CANCELED){
            throw new UserException(CANCELED_ORDER_CAN_NOT_BE_CANCELED_MESSAGE);
        }
        order.setStatus(OrderStatus.CANCELED);
        sqlProcessor.update(order);
    }

    public List<ManagerOrderDescription> getOrders(Calendar startDate, Calendar finishDate, long hotelId) throws Exception {
        Object[] args = new Object[]{hotelId, startDate, finishDate};
        List<ManagerOrderDescription> orders = sqlProcessor.executeSelect(ManagerOrderDescription.class, SELECT_ORDERS_IN_HOTEL, args);
        return orders;
    }

    public void createOrUpdateRoom(Room room) throws Exception {
        RoomType roomType = sqlProcessor.getByID(RoomType.class, room.getType());
        if (roomType == null || roomType.getStatus() != RoomTypeStatus.WORKED){
            throw new UserException(ROOM_TYPE_DOES_NOT_EXIST_MESSAGE, UserErrorCode.BAD_PARAMETERS);
        }
        if (room.getId() == null){
            sqlProcessor.insert(room);
        }else {
            Room roomDB = sqlProcessor.getByID(Room.class, room.getId());
            if (roomDB == null || !roomDB.getHotelId().equals(room.getHotelId())){
                throw new UserException(ROOM_DOES_NOT_EXIST_MESSAGE, UserErrorCode.BAD_PARAMETERS);
            }
            sqlProcessor.update(room);
        }
    }
}
