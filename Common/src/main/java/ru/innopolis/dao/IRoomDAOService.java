package ru.innopolis.dao;

import org.multylanguages.exeption.MetaMessageException;
import ru.innopolis.dao.entity.Client;
import ru.innopolis.dao.entity.Order;
import ru.innopolis.dao.entity.OrderDescription;
import ru.innopolis.dao.entity.Room;
import ru.innopolis.dao.entity.addition.ExtendedRoom;
import ru.innopolis.dao.entity.addition.ManagerOrderDescription;

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
    List<ExtendedRoom> getFreeRoomsInHotel(long hotelId, long roomTypeId, Calendar from, Calendar to) throws Exception;

    /**
     * Зарезервировать комнату для клиента
     * @param client Клиент
     * @param roomID ИД комнаты
     * @param from Дата заезда
     * @param to Дата выезда
     * @throws MetaMessageException Не верные входные параметры
     */
    void bookRoom(Client client, long roomID, Calendar from, Calendar to) throws Exception;

    /**
     * Отменить заказ
     * @param client Клиент
     * @param orderId Номер заказа
     * @throws Exception Не удалось отменить заказ
     */
    void cancelBook(Client client, long orderId) throws Exception;

    /**
     * Отменить заказ
     * @param orderID ИД заказа
     * @param hotelID ИД отеля в котором был сделан заказ
     * @throws Exception Не смогли отменить заказ
     */
    void cancelOrder(long orderID, long hotelID) throws Exception;
    /**
     * Получить список заказов клиента
     * @param client Клиент
     * @return Список заказов клиента
     * @throws Exception Не удалось извлечь заказы
     */
    List<OrderDescription> getOrdersByClient(Client client) throws Exception;

    /**
     * Оплатить заказ
     * @param orderID ИД заказа
     * @param hotelID ИД отеля в котором был сделан заказ
     * @throws Exception Не смогли оплатить
     */
    void payRoom(long orderID, long hotelID) throws Exception;

    /**
     * Получить список заказов с подробным описанием за период
     * @param startDate Начала периода
     * @param finishDate Конец период
     * @param hotelId ИД отеля в рамках которого нужно производить поиск
     * @return Список заказов с подробным описанием
     * @throws Exception Не смогли получить список заказов
     */
    List<ManagerOrderDescription> getOrders(Calendar startDate, Calendar finishDate, long hotelId) throws Exception;

    void createOrUpdateRoom(Room room) throws Exception;
}
