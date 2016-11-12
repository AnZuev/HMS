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
import ru.innopolis.dao.IHotelDAOService;
import ru.innopolis.dao.entity.Employee;
import ru.innopolis.dao.entity.RoomType;
import ru.innopolis.dao.entity.addition.RoomTypeStatus;
import ru.innopolis.exceptions.UserException;
import ru.innopolis.models.DeleteRoomTypeModelRequest;
import ru.innopolis.models.EmployeeRoomTypeResponseModel;
import ru.innopolis.models.RoomTypeModelRequest;
import org.springframework.beans.BeanUtils;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Создано: Денис
 * Дата:  11.11.2016
 * Описание: Контроле по работе с сущностью "Тип комнаты"
 */
@RestController
@RequestMapping("/private/roomtype")
public class RoomTypeController extends BaseRestController {

    private Logger logger = Logger.getLogger(RoomTypeController.class.getName());

    @Autowired
    public RoomTypeController(MessageSource messageSource) {
        super(messageSource);
    }

    /**
     * Создать или обновить сущность "Тип комнаты". Для обновления нужно прислать ИД сущности.
     *
     * @param modelRequest Запрос на создание/обновление
     * @param errors       Список ошибок валидации
     * @param session      Сессия
     * @return Код 200, если всё прошло успешно. В противном случае, описание ошибки.
     */
    @PostMapping("/update")
    public ResponseEntity createOrUpdate(@Valid @RequestBody RoomTypeModelRequest modelRequest, Errors errors, HttpSession session) {
        ResponseEntity response = getValidationErrorResponse(errors);
        if (response == null) {
            Employee employee = (Employee) session.getAttribute(AuthorizationConstant.EMPLOYEE_KEY);
            RoomType roomType = buildRoomType(modelRequest, employee);
            try {
                IHotelDAOService service = DAOServiceFactory.getInstance().createService(IHotelDAOService.class);
                service.createOrUpdateRoomType(roomType);
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

    private RoomType buildRoomType(@Valid @RequestBody RoomTypeModelRequest modelRequest, Employee employee) {
        RoomType roomType = new RoomType();
        BeanUtils.copyProperties(modelRequest, roomType);
        roomType.setHotelId(employee.getHotelId());
        roomType.setStatus(RoomTypeStatus.WORKED);
        return roomType;
    }

    /**
     * Удалить сущность "Тип комнаты"
     *
     * @param modelRequest Запрос на удаление
     * @param errors       Список ошибок валидации
     * @param session      Сессия
     * @return Код 200, если всё ок. В противном случае, описание ошибки
     */
    @PostMapping("/delete")
    public ResponseEntity delete(@Valid @RequestBody DeleteRoomTypeModelRequest modelRequest, Errors errors, HttpSession session) {
        ResponseEntity response = getValidationErrorResponse(errors);
        if (response == null) {
            Employee employee = (Employee) session.getAttribute(AuthorizationConstant.EMPLOYEE_KEY);
            try {
                IHotelDAOService service = DAOServiceFactory.getInstance().createService(IHotelDAOService.class);
                service.deleteRoomType(modelRequest.getRoomTypeId(), employee.getHotelId());
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

    /**
     * Получить список сущностей "Тип комнаты"
     *
     * @param session Сессия
     * @return Список комнат
     */
    @GetMapping("/all")
    public ResponseEntity getAll(HttpSession session) {
        ResponseEntity response;
        Employee employee = (Employee) session.getAttribute(AuthorizationConstant.EMPLOYEE_KEY);
        try {
            IHotelDAOService service = DAOServiceFactory.getInstance().createService(IHotelDAOService.class);
            List<RoomType> roomTypes = service.getRoomTypesByHotelId(employee.getHotelId());
            List<EmployeeRoomTypeResponseModel> result = new ArrayList<>(roomTypes.size());
            roomTypes.forEach(r -> {
                EmployeeRoomTypeResponseModel model = new EmployeeRoomTypeResponseModel();
                BeanUtils.copyProperties(r, model);
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
