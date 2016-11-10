package ru.innopolis.controllers;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.innopolis.constants.AuthorizationConstant;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IEmployeeDAOService;
import ru.innopolis.dao.entity.Employee;
import ru.innopolis.exceptions.UserException;
import ru.innopolis.helpers.PasswordHelper;
import ru.innopolis.models.CreateEditEmployeeModelRequest;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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

    private Logger logger = Logger.getLogger(EmployeeController.class.getName());

    public EmployeeController(MessageSource messageSource) {
        super(messageSource);
    }

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

    private Employee convertToEmployee(CreateEditEmployeeModelRequest model) throws Exception {
        Employee employee = new Employee();
        employee.setId(model.getId());
        employee.setFirstName(model.getFirstName());
        employee.setSecondName(model.getSecondName());
        employee.setFatherName(model.getFatherName());
        employee.setMail(model.getEmail());
        String encryptPassword = PasswordHelper.encrypt(model.getPassword());
        employee.setPassword(encryptPassword);
        employee.setType(Employee.Type.MANAGER);
        return employee;
    }
}
