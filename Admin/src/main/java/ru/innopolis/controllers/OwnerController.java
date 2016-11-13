package ru.innopolis.controllers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IEmployeeDAOService;
import ru.innopolis.dao.entity.Employee;
import ru.innopolis.exceptions.UserException;
import ru.innopolis.helpers.PasswordHelper;
import ru.innopolis.models.CreateEditHotelOwnerModelRequest;
import ru.innopolis.models.OwnerIdResponseModel;
import ru.innopolis.models.OwnerResponseModel;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
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
     *
     * @param modelRequest Модель запроса на создание
     * @param errors       Список ошибок валидации
     * @return Http код 200, если всё прошло хорошо. В противном случае описание ошибки.
     */
    @PostMapping("/owner/update")
    public ResponseEntity createOrUpdate(@Valid @RequestBody CreateEditHotelOwnerModelRequest modelRequest, Errors errors) {
        ResponseEntity response = getValidationErrorResponse(errors);
        if (response == null) {
            try {
                Employee employee = convertToEmployee(modelRequest);
                IEmployeeDAOService service = DAOServiceFactory.getInstance().createService(IEmployeeDAOService.class);
                service.addOrUpdate(employee);
                if(modelRequest.getId() == null){
                    OwnerIdResponseModel model = new OwnerIdResponseModel();
                    model.setOwnerID(employee.getId());
                    response = new ResponseEntity(model, HttpStatus.OK);
                }else {
                    response = new ResponseEntity(HttpStatus.OK);
                }

            } catch (UserException e) {
                response = handleUserException(e);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                response = getGeneralErrorResponse();
            }
        }
        return response;
    }

    private Employee convertToEmployee(CreateEditHotelOwnerModelRequest model) throws Exception {
        Employee employee = new Employee();
        BeanUtils.copyProperties(model, employee);
        employee.setMail(model.getEmail());
        String password = model.getPassword();
        if (StringUtils.isNotEmpty(password)){
            String encryptPassword = PasswordHelper.encrypt(password);
            employee.setPassword(encryptPassword);
        }
        employee.setType(Employee.Type.OWNER);
        return employee;
    }

    /**
     * Получить список владельцев отелей
     * @return Список владельцев отелей
     */
    @GetMapping("/owner/all")
    public ResponseEntity getListOfOwners() {
        ResponseEntity response = null;
        try {
            IEmployeeDAOService service = DAOServiceFactory.getInstance().createService(IEmployeeDAOService.class);
            List<Employee> owners = service.getOwners();
            List<OwnerResponseModel> models = new ArrayList<>(owners.size());
            owners.forEach(o -> {
                OwnerResponseModel model = new OwnerResponseModel();
                BeanUtils.copyProperties(o, model);
                models.add(model);
            });
            HttpStatus status = owners.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            response = new ResponseEntity(models, status);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            response = getGeneralErrorResponse();
        }
        return response;
    }

}
