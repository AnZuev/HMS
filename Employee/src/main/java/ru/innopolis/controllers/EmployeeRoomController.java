package ru.innopolis.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.innopolis.constants.AuthorizationConstant;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IRoomDAOService;
import ru.innopolis.dao.entity.Employee;
import ru.innopolis.dao.entity.Room;
import ru.innopolis.dao.entity.RoomStatus;
import ru.innopolis.exceptions.UserException;
import ru.innopolis.models.RoomModelRequest;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Создано: Денис
 * Дата:  11.11.2016
 * Описание:
 */
@RestController
@RequestMapping("/private/room")
public class EmployeeRoomController extends BaseRestController {

    private Logger logger = Logger.getLogger(EmployeeRoomController.class.getName());

    @Autowired
    public EmployeeRoomController(MessageSource messageSource) {
        super(messageSource);
    }

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
        room.setHotelId(employee.getHotelId());
        RoomStatus status = Enum.valueOf(RoomStatus.class, modelRequest.getStatus());
        room.setStatus(status);
        return room;
    }
}
