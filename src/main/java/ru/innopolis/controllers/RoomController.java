package ru.innopolis.controllers;

import org.multylanguages.exeption.MetaMessageException;
import org.multylanguages.message.MetaMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.innopolis.constants.AuthorizationConstant;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IRoomDAOService;
import ru.innopolis.dao.entity.Client;
import ru.innopolis.dao.entity.Room;
import ru.innopolis.dao.imp.RoomDAOService;
import ru.innopolis.models.AvailableRoomRequestModel;
import ru.innopolis.models.AvailableRoomResponseModel;
import ru.innopolis.models.BookModelRequest;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.innopolis.constants.AuthorizationConstant.CLIENT_KEY;

/**
 * Создано: Денис
 * Дата:  01.11.2016
 * Описание: Контроле по работе с номерами
 */
@RestController
public class RoomController extends BaseRestController {
    private Logger logger = Logger.getLogger(AuthorizationController.class.getName());

    @Autowired
    public RoomController(MessageSource messageSource) {
        super(messageSource);
    }

    /**
     * Получить список свободных комнат в отеле в указанный период
     * @param modelRequest Параметры выборки
     * @param errors Список ошибок валидации
     * @return Список свободных комнат. В случае ошибки - информация об ошибке
     */
    @GetMapping("/rooms/getAvailableRooms")
    public ResponseEntity findFreeRooms(@Valid AvailableRoomRequestModel modelRequest, Errors errors){
        ResponseEntity response = getValidationErrorResponse(errors);
        if (response == null){
            long hotelId = modelRequest.getHotelId();
            long roomTypeId = modelRequest.getRoomTypeId();
            Calendar from = modelRequest.getFrom();
            Calendar to = modelRequest.getTo();
            try {
                IRoomDAOService service = DAOServiceFactory.getInstance().createService(RoomDAOService.class);
                List<Room> freeRooms = service.getFreeRoomsInHotel(hotelId, roomTypeId, from, to);
                List<AvailableRoomResponseModel> responseModelList = new ArrayList<>(freeRooms.size());
                freeRooms.forEach(room -> {
                    AvailableRoomResponseModel model = new AvailableRoomResponseModel();
                    model.setId(room.getId());
                    model.setRoomNumber(room.getRoomNumber());
                    responseModelList.add(model);
                });
                HttpStatus status = freeRooms.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
                response = new ResponseEntity(responseModelList, status);
            }catch (Exception e){
                logger.log(Level.SEVERE, e.getMessage(), e);
                response = getGeneralErrorResponse();
            }
        }
        return response;
    }

    /**
     * Зарезервировать номер
     * @param modelRequest Запрос на резервацию
     * @param errors Список ошибок валидации
     * @param session Сессия
     * @return {@link HttpStatus#OK} если бронь прошла успешно, в противном случае ошибка
     */
    @PutMapping("/private/rooms/book")
    public ResponseEntity bookRoom(@Valid @RequestBody BookModelRequest modelRequest, Errors errors, HttpSession session){
        ResponseEntity response = getValidationErrorResponse(errors);
        if (response == null){
            long roomId = modelRequest.getRoomId();
            Calendar from = modelRequest.getFrom();
            Calendar to = modelRequest.getTo();
            try {
                IRoomDAOService service = DAOServiceFactory.getInstance().createService(RoomDAOService.class);
                Client client = (Client) session.getAttribute(AuthorizationConstant.CLIENT_KEY);
                try {
                    service.bookRoom(client, roomId, from, to);
                    response = new ResponseEntity(HttpStatus.OK);
                }catch (MetaMessageException e){
                    MetaMessage metaMessage = e.getMetaMessage();
                    response = getErrorResponse(metaMessage);
                }
            }catch (Exception e){
                logger.log(Level.SEVERE, e.getMessage(), e);
                response = getGeneralErrorResponse();
            }
        }
        return response;
    }
}
