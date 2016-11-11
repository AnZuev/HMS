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
import ru.innopolis.dao.IHotelDAOService;
import ru.innopolis.dao.entity.Employee;
import ru.innopolis.dao.entity.RoomType;
import ru.innopolis.exceptions.UserException;
import ru.innopolis.models.RoomTypeModelRequest;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Создано: Денис
 * Дата:  11.11.2016
 * Описание: Контроле по работе с сущнссть "Тип комнаты"
 */
@RestController
@RequestMapping("/private/roomtype")
public class RoomTypeController extends BaseRestController {

    private Logger logger = Logger.getLogger(RoomTypeController.class.getName());

    @Autowired
    public RoomTypeController(MessageSource messageSource) {
        super(messageSource);
    }

    @PostMapping("/update")
    public ResponseEntity createOrUpdate(@Valid @RequestBody RoomTypeModelRequest modelRequest, Errors errors, HttpSession session){
        ResponseEntity response = getValidationErrorResponse(errors);
        if (response == null){
            Employee employee = (Employee) session.getAttribute(AuthorizationConstant.EMPLOYEE_KEY);
            RoomType roomType = new RoomType();
            BeanUtils.copyProperties(modelRequest, roomType);
            roomType.setHotelId(employee.getHotelId());

            try {
                IHotelDAOService service = DAOServiceFactory.getInstance().createService(IHotelDAOService.class);
                service.createOrUpdateRoomType(roomType);
                response = new ResponseEntity(HttpStatus.OK);
            }catch (UserException e) {
                response = handleUserException(e);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                response = getGeneralErrorResponse();
            }
        }
        return response;
    }
}
