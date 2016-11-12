package ru.innopolis.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.innopolis.constants.AuthorizationConstant;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IRoomDAOService;
import ru.innopolis.dao.entity.Employee;
import ru.innopolis.dao.entity.Room;
import ru.innopolis.dao.entity.RoomStatus;
import ru.innopolis.dao.entity.addition.ExtendedRoom;
import ru.innopolis.exceptions.UserException;
import ru.innopolis.models.RoomModelRequest;
import ru.innopolis.models.RoomModelResponse;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Создано: Денис
 * Дата:  11.11.2016
 * Описание: Контроллер по работе с сотрудниками
 */
@RestController
@RequestMapping("/private/room")
public class EmployeeRoomController extends BaseRestController {

    private Logger logger = Logger.getLogger(EmployeeRoomController.class.getName());

    @Autowired
    public EmployeeRoomController(MessageSource messageSource) {
        super(messageSource);
    }

    /**
     * Создать или обновить сущность "Комната"
     *
     * @param modelRequest Запрос на изменение сущности
     * @param errors       Список ошибок валидации
     * @param session      Сессия
     * @return Код 200, если всё ок. В противном случае, описание ошибки
     */
    @PostMapping("/update")
    public ResponseEntity createOrUpdate(@Valid @RequestBody RoomModelRequest modelRequest, Errors errors, HttpSession session) {
        ResponseEntity response = getValidationErrorResponse(errors);
        if (response == null) {
            Employee employee = (Employee) session.getAttribute(AuthorizationConstant.EMPLOYEE_KEY);
            Room room = buildRoom(modelRequest, employee);
            try {
                IRoomDAOService service = DAOServiceFactory.getInstance().createService(IRoomDAOService.class);
                service.createOrUpdateRoom(room);
                response = new ResponseEntity(HttpStatus.OK);
            } catch (UserException e) {
                response = handleUserException(e);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                response = getGeneralErrorResponse();
            }
        }
        return response;
    }

    private Room buildRoom(RoomModelRequest modelRequest, Employee employee) {
        Room room = new Room();
        BeanUtils.copyProperties(modelRequest, room);
        room.setType(modelRequest.getTypeID());
        room.setHotelId(employee.getHotelId());
        RoomStatus status = Enum.valueOf(RoomStatus.class, modelRequest.getStatus());
        room.setStatus(status);
        return room;
    }

    /**
     * Получить все номера в отеле
     * @param session Сессия
     * @return Список номеров в отеле
     */
    @GetMapping("/all")
    public ResponseEntity getAll(HttpSession session) {
        ResponseEntity response;
        Employee employee = (Employee) session.getAttribute(AuthorizationConstant.EMPLOYEE_KEY);
        try {
            IRoomDAOService service = DAOServiceFactory.getInstance().createService(IRoomDAOService.class);
            List<ExtendedRoom> rooms = service.getAllRoomByHotelId(employee.getHotelId());
            List<RoomModelResponse> result = new ArrayList<>(rooms.size());
            rooms.forEach(r -> {
                RoomModelResponse model = new RoomModelResponse();
                BeanUtils.copyProperties(r, model);
                model.setStatus(r.getStatus().name());
                result.add(model);
            });
            HttpStatus status = result.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            response = new ResponseEntity(result, status);
        } catch (UserException e) {
            response = handleUserException(e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            response = getGeneralErrorResponse();
        }
        return response;
    }
}
