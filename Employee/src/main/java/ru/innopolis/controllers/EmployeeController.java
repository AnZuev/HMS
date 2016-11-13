package ru.innopolis.controllers;

import org.apache.commons.lang3.StringUtils;
import org.multylanguages.message.MetaMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.innopolis.constants.AuthorizationConstant;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IEmployeeDAOService;
import ru.innopolis.dao.entity.Employee;
import ru.innopolis.exceptions.UserErrorCode;
import ru.innopolis.exceptions.UserException;
import ru.innopolis.helpers.PasswordHelper;
import ru.innopolis.models.CreateEditEmployeeModelRequest;
import ru.innopolis.models.DeleteEmployeeModelRequest;
import ru.innopolis.models.EmployeeIdResponseModel;
import ru.innopolis.models.EmployeeResponseModel;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Создано: Денис
 * Дата:  10.11.2016
 * Описание: Контроллер по работе со сотрудниками
 */
@RestController
@RequestMapping("/private/owner/employees")
public class EmployeeController extends BaseRestController {

    private static final MetaMessage CAN_NOT_DELETE_MESSAGE = new MetaMessage("you.can.not.delete.yourself");

    private Logger logger = Logger.getLogger(EmployeeController.class.getName());

    public EmployeeController(MessageSource messageSource) {
        super(messageSource);
    }

    /**
     * Обновить или создать сотрудника. Решение об обновлении принимается на основе наличия ИД.
     * @param modelRequest Запрос на изменение/создание
     * @param errors Список ошибок валидации
     * @param session Сессия
     * @return Код 200, если всё прошло успешно. В противном случае, описание ошибки.
     */
    @PutMapping("/update")
    public ResponseEntity createNewEmployee(@Valid @RequestBody CreateEditEmployeeModelRequest modelRequest, Errors errors, HttpSession session) {
        ResponseEntity response = getValidationErrorResponse(errors);
        if (response == null) {
            try {
                Employee owner = (Employee) session.getAttribute(AuthorizationConstant.EMPLOYEE_KEY);
                Employee employee = convertToEmployee(modelRequest);
                employee.setHotelId(owner.getHotelId());
                IEmployeeDAOService service = DAOServiceFactory.getInstance().createService(IEmployeeDAOService.class);
                service.addOrUpdate(employee);
                if(modelRequest.getId() == null){
                    EmployeeIdResponseModel model = new EmployeeIdResponseModel();
                    model.setEmployeeID(employee.getId());
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

    /**
     * Получить список сотрудников в рамках отеля
     * @param session Сессия
     * @return Список сотрудников
     */
    @GetMapping("/all")
    public ResponseEntity getAll(HttpSession session){
        Employee owner = (Employee) session.getAttribute(AuthorizationConstant.EMPLOYEE_KEY);
        ResponseEntity response;
        try {
            IEmployeeDAOService service = DAOServiceFactory.getInstance().createService(IEmployeeDAOService.class);
            List<Employee> manages = service.getManagersByHotelId(owner.getHotelId());
            List<EmployeeResponseModel> responseModel = new ArrayList<>(manages.size());
            manages.forEach(emp->{
                EmployeeResponseModel model = new EmployeeResponseModel();
                BeanUtils.copyProperties(emp, model);
                model.setType(emp.getType().name());
                responseModel.add(model);
            });

            HttpStatus status = responseModel.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            response = new ResponseEntity(responseModel, status);
        } catch (UserException e) {
            response = handleUserException(e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            response = getGeneralErrorResponse();
        }

        return response;
    }

    /**
     * Удалить сотрудника
     * @param modelRequest Модель запроса на удаление
     * @param errors Список ошибок валидации
     * @param session Сессия
     * @return 200, если всё ок. В противном случае описание ошибки
     */
    @PostMapping("/delete")
    public ResponseEntity deleteEmployee(@Valid @RequestBody DeleteEmployeeModelRequest modelRequest, Errors errors, HttpSession session){
        ResponseEntity response = getValidationErrorResponse(errors);
        if (response == null) {
            try {
                Employee employee = getEmployee(modelRequest, session);
                IEmployeeDAOService service = DAOServiceFactory.getInstance().createService(IEmployeeDAOService.class);
                service.delete(employee);
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

    private Employee getEmployee(DeleteEmployeeModelRequest modelRequest, HttpSession session) throws UserException {
        Employee owner = (Employee) session.getAttribute(AuthorizationConstant.EMPLOYEE_KEY);
        if (owner.getId().equals(modelRequest.getEmployeeId())){
            throw new UserException(CAN_NOT_DELETE_MESSAGE, UserErrorCode.BAD_PARAMETERS);
        }
        Employee employee = new Employee();
        employee.setId(modelRequest.getEmployeeId());
        employee.setHotelId(owner.getHotelId());
        return employee;
    }

    private Employee convertToEmployee(CreateEditEmployeeModelRequest model) throws Exception {
        Employee employee = new Employee();
        BeanUtils.copyProperties(model, employee);
        String password = model.getPassword();
        if (StringUtils.isNotEmpty(password)){
            String encryptPassword = PasswordHelper.encrypt(password);
            employee.setPassword(encryptPassword);
        }
        employee.setType(Employee.Type.MANAGER);
        return employee;
    }
}
