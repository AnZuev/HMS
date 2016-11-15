package ru.innopolis.dao.imp;

import org.multylanguages.message.MetaMessage;
import ru.innopolis.dao.IRoomDAOService;
import ru.innopolis.dao.entity.*;
import ru.innopolis.dao.entity.addition.ExtendedRoom;
import ru.innopolis.dao.entity.addition.ManagerOrderDescription;
import ru.innopolis.dao.entity.addition.OrderDescription;
import ru.innopolis.dao.entity.addition.RoomTypeStatus;
import ru.innopolis.dao.processor.ISQLProcessor;
import ru.innopolis.exceptions.UserErrorCode;
import ru.innopolis.exceptions.UserException;

import java.util.*;

/**
 * Создано: Денис
 * Дата:  01.11.2016
 * Описание: Реализация сервиса по работе с комнатой, хранящейся в БД
 */
public class RoomDAOService implements IRoomDAOService {

    private static final MetaMessage ROOM_IS_NOT_FREE_MESSAGE = new MetaMessage("room.is.not.free");
    private static final MetaMessage ORDER_CAN_NOT_BE_CANCELED_MESSAGE = new MetaMessage("order.can.not.be.canceled");
    private static final MetaMessage ORDER_IS_NOT_FOUND_MESSAGE = new MetaMessage("order.is.not.found");
    private static final MetaMessage ORDER_IS_PAID_MESSAGE = new MetaMessage("order.is.paid");
    private static final MetaMessage ORDER_IS_CANCELED_MESSAGE = new MetaMessage("order.is.canceled");
    private static final MetaMessage ROOM_DOES_NOT_EXIST_MESSAGE = new MetaMessage("room.does.not.exist");
    private static final MetaMessage ROOM_TYPE_DOES_NOT_EXIST_MESSAGE = new MetaMessage("room.type.does.not.exist");
    private static final MetaMessage PAID_ORDER_CAN_NOT_BE_CANCELED_MESSAGE = new MetaMessage("paid.order.can.not.be.canceled");
    private static final MetaMessage CANCELED_ORDER_CAN_NOT_BE_CANCELED_MESSAGE = new MetaMessage("canceled.order.can.not.be.canceled");
    private static final String FREE_ROOM_SELECT = "freeRoomSelect";
    private static final String CHECK_IS_ROOM_FREE = "checkIsRoomFree";
    private static final String SELECT_CLIENT_ORDERS = "SELECT_CLIENT_ORDERS";
    private static final String SELECT_ORDERS_IN_HOTEL = "SELECT_ORDERS_IN_HOTEL";
    private static final String ALL_ROOM_FOR_MANAGER_SELECT = "allRoomForManagerSelect";
    private static final String HOTEL_ID_ATTRIBUTE_NAME = "HOTEL_ID";
    private static final String ROOM_TYPE_ID_ATTRIBUTE_NAME = "ROOM_TYPE_ID";
    private static final String START_DATE_ATTRIBUTE_NAME = "START_DATE";
    private static final String FINISH_DATE_ATTRIBUTE_NAME = "FINISH_DATE";
    private static final String ROOM_ID_ATTRIBUTE_NAME = "ROOM_ID";
    private static final String CLIENT_ID_ATTRIBUTE_NAME = "CLIENT_ID";
    private ISQLProcessor sqlProcessor;


    public RoomDAOService(ISQLProcessor sqlProcessor) {
        this.sqlProcessor = sqlProcessor;
    }

    public List<ExtendedRoom> getFreeRoomsInHotel(long hotelId, long roomTypeId, Calendar from, Calendar to) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put(HOTEL_ID_ATTRIBUTE_NAME, hotelId);
        map.put(ROOM_TYPE_ID_ATTRIBUTE_NAME, roomTypeId);
        map.put(START_DATE_ATTRIBUTE_NAME, from);
        map.put(FINISH_DATE_ATTRIBUTE_NAME, to);

        List<ExtendedRoom> list = sqlProcessor.executeSelect(ExtendedRoom.class, FREE_ROOM_SELECT, map);
        return list;
    }

    public void bookRoom(Client client, long roomID, Calendar from, Calendar to) throws Exception {

        Room room = sqlProcessor.getByID(Room.class, roomID);

        if (room == null) {
            throw new UserException(ROOM_DOES_NOT_EXIST_MESSAGE, UserErrorCode.BAD_PARAMETERS);
        }

        Map<String, Object> map = new HashMap<>();
        map.put(HOTEL_ID_ATTRIBUTE_NAME, room.getHotelId());
        map.put(ROOM_ID_ATTRIBUTE_NAME, room.getId());
        map.put(START_DATE_ATTRIBUTE_NAME, from);
        map.put(FINISH_DATE_ATTRIBUTE_NAME, to);

        List<Order> list = sqlProcessor.executeSelect(Order.class, CHECK_IS_ROOM_FREE, map);

        if (!list.isEmpty()) {
            throw new UserException(ROOM_IS_NOT_FREE_MESSAGE, UserErrorCode.BAD_PARAMETERS);
        }

        long days = calculateCountOfDays(from, to);


        RoomType type = sqlProcessor.getByID(RoomType.class, room.getType());

        Order order = new Order();
        order.setClientId(client.getId());
        order.setRoomId(roomID);
        order.setHotelId(room.getHotelId());
        order.setStartDate(from);
        order.setFinishDate(to);
        order.setStatus(OrderStatus.BOOKED);
        order.setCost(days * type.getCost());

        sqlProcessor.insert(order);

    }

    /**
     * Посчитать разницу между датами заезда и выезда в днях
     * @param start Дата заезда
     * @param finish Дата выезда
     * @return Количество дней проживания
     */
    private long calculateCountOfDays(Calendar start, Calendar finish) {
        long milSeck = finish.getTimeInMillis() - start.getTimeInMillis();
        return milSeck / 86400000;
    }

    public void cancelBook(Client client, long orderId) throws Exception {
        Order order = sqlProcessor.getByID(Order.class, orderId);
        if (order == null || !order.getClientId().equals(client.getId())) {
            throw new UserException(ORDER_IS_NOT_FOUND_MESSAGE, UserErrorCode.NOT_FOUND);
        }

        if (order.getStatus() != OrderStatus.BOOKED) {
            throw new UserException(ORDER_CAN_NOT_BE_CANCELED_MESSAGE, UserErrorCode.BAD_PARAMETERS);
        }
        order.setStatus(OrderStatus.CANCELED);
        sqlProcessor.update(order);
    }

    public List<OrderDescription> getOrdersByClient(Client client) throws Exception {
        Map<String, Object> map = Collections.singletonMap(CLIENT_ID_ATTRIBUTE_NAME, new Long(client.getId()));
        return sqlProcessor.executeSelect(OrderDescription.class, SELECT_CLIENT_ORDERS, map);
    }

    public void payRoom(long orderID, long hotelID) throws Exception {
        Order order = sqlProcessor.getByID(Order.class, orderID);
        if (order == null || hotelID != order.getHotelId()) {
            throw new UserException(ORDER_IS_NOT_FOUND_MESSAGE, UserErrorCode.NOT_FOUND);
        }
        if (order.getStatus() == OrderStatus.PAID) {
            throw new UserException(ORDER_IS_PAID_MESSAGE, UserErrorCode.BAD_PARAMETERS);
        }
        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new UserException(ORDER_IS_CANCELED_MESSAGE, UserErrorCode.BAD_PARAMETERS);
        }
        order.setStatus(OrderStatus.PAID);
        sqlProcessor.update(order);
    }

    public void cancelOrder(long orderID, long hotelID) throws Exception {
        Order order = sqlProcessor.getByID(Order.class, orderID);
        if (order == null || hotelID != order.getHotelId()) {
            throw new UserException(ORDER_IS_NOT_FOUND_MESSAGE, UserErrorCode.NOT_FOUND);
        }
        if (order.getStatus() == OrderStatus.PAID) {
            throw new UserException(PAID_ORDER_CAN_NOT_BE_CANCELED_MESSAGE, UserErrorCode.BAD_PARAMETERS);
        }
        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new UserException(CANCELED_ORDER_CAN_NOT_BE_CANCELED_MESSAGE, UserErrorCode.BAD_PARAMETERS);
        }
        order.setStatus(OrderStatus.CANCELED);
        sqlProcessor.update(order);
    }

    public List<ManagerOrderDescription> getOrders(Calendar startDate, Calendar finishDate, long hotelId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put(HOTEL_ID_ATTRIBUTE_NAME, hotelId);
        map.put(START_DATE_ATTRIBUTE_NAME, startDate);
        map.put(FINISH_DATE_ATTRIBUTE_NAME, finishDate);
        List<ManagerOrderDescription> orders = sqlProcessor.executeSelect(ManagerOrderDescription.class, SELECT_ORDERS_IN_HOTEL, map);
        return orders;
    }

    public void createOrUpdateRoom(Room room) throws Exception {
        RoomType roomType = sqlProcessor.getByID(RoomType.class, room.getType());
        if (roomType == null || roomType.getStatus() != RoomTypeStatus.WORKED) {
            throw new UserException(ROOM_TYPE_DOES_NOT_EXIST_MESSAGE, UserErrorCode.BAD_PARAMETERS);
        }
        if (room.getId() == null) {
            sqlProcessor.insert(room);
        } else {
            Room roomDB = sqlProcessor.getByID(Room.class, room.getId());
            if (roomDB == null || !roomDB.getHotelId().equals(room.getHotelId())) {
                throw new UserException(ROOM_DOES_NOT_EXIST_MESSAGE, UserErrorCode.BAD_PARAMETERS);
            }
            sqlProcessor.update(room);
        }
    }

    public List<ExtendedRoom> getAllRoomByHotelId(long hotelId) throws Exception {
        Map<String, Object> map = Collections.singletonMap(HOTEL_ID_ATTRIBUTE_NAME, new Long(hotelId));
        List<ExtendedRoom> extendedRooms = sqlProcessor.executeSelect(ExtendedRoom.class, ALL_ROOM_FOR_MANAGER_SELECT, map);
        return extendedRooms;
    }
}
