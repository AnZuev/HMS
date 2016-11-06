package ru.innopolis.controllers;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IEmployeeDAOService;
import ru.innopolis.dao.entity.Employee;
import ru.innopolis.exceptions.UserException;
import ru.innopolis.models.CreateEditHotelOwnerModelRequest;

import javax.validation.Valid;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Создано: Денис
 * Дата:  06.11.2016
 * Описание: Контроллер создания нового владельца отеля
 */
@RestController()
@RequestMapping("/private")
public class OwnerController extends BaseRestController {

    private Logger logger = java.util.logging.Logger.getLogger(OwnerController.class.getName());

    public OwnerController(MessageSource messageSource) {
        super(messageSource);
    }

    /**
     * Создать или обновить владельца отеля
     * @param modelRequest Модель запроса на создание
     * @param errors Список ошибок валидации
     * @return Http код 200, если всё прошло хорошо. В противном случае описание ошибки.
     */
    @PostMapping("/owner/update")
    public ResponseEntity createOrUpdate(@Valid @RequestBody CreateEditHotelOwnerModelRequest modelRequest, Errors errors) {
        ResponseEntity response = getValidationErrorResponse(errors);
        if (response == null) {
            Employee employee = convertToEmployee(modelRequest);
            try {
                IEmployeeDAOService service = DAOServiceFactory.getInstance().createService(IEmployeeDAOService.class);
                service.addOrUpdate(employee);
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

    private Employee convertToEmployee(CreateEditHotelOwnerModelRequest model) {
        Employee employee = new Employee();
        employee.setId(model.getId());
        employee.setHotelId(model.getHotelId());
        employee.setFirstName(model.getFirstName());
        employee.setSecondName(model.getSecondName());
        employee.setFatherName(model.getFatherName());
        employee.setMail(model.getEmail());
        employee.setPassword(model.getPassword());
        employee.setType(Employee.Type.OWNER);
        return employee;
    }

}
