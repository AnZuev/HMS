package ru.innopolis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IRoomDAOService;
import ru.innopolis.dao.entity.Room;
import ru.innopolis.dao.imp.RoomDAOService;
import ru.innopolis.models.AvailableRoomRequestModel;
import ru.innopolis.models.AvailableRoomResponseModel;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
}
