package ru.innopolis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.innopolis.constants.AuthorizationConstant;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IEmployeeDAOService;
import ru.innopolis.dao.entity.Employee;
import ru.innopolis.helpers.PasswordHelper;
import ru.innopolis.models.AuthorizationRequestModel;
import ru.innopolis.models.AuthorizationResponseModel;
import ru.innopolis.models.ErrorResponse;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Создано: Денис
 * Дата:  29.10.2016
 * Описание: Котроллер аутентификации
 */

@RestController
public class EmployeeAuthorizationController extends BaseRestController {

    private static final String EMPLOYEE_DOES_NOT_EXIT = "employee.does.not.exit";
    private Logger logger = Logger.getLogger(EmployeeAuthorizationController.class.getName());

    @Autowired
    public EmployeeAuthorizationController(MessageSource messageSource) {
        super(messageSource);
    }

    /**
     * Пройти аутентицикацию
     * @param model Модель запроса на аутентификацию
     * @param errors Результат валидации
     * @param session Сессия
     * @return Информация о сотруднике, если он существует. В противном случае описание ошибки.
     */
    @PostMapping("/auth/login")
    public ResponseEntity authenticate(@Valid @RequestBody AuthorizationRequestModel model, Errors errors, HttpSession session) {
        ResponseEntity response = getValidationErrorResponse(errors);
        if (response == null) {
            try {
                String password = model.getPassword();
                String encryptPassword = PasswordHelper.encrypt(password);

                IEmployeeDAOService service = DAOServiceFactory.getInstance().createService(IEmployeeDAOService.class);
                Employee employee = service.findEmployee(model.getMail(), encryptPassword);
                if (employee != null) {
                    session.setAttribute(AuthorizationConstant.AUTHORIZATION_KEY, Boolean.TRUE);
                    session.setAttribute(AuthorizationConstant.EMPLOYEE_KEY, employee);
                    AuthorizationResponseModel responseModel = buildResponseModel(employee);
                    response = new ResponseEntity<>(responseModel, HttpStatus.OK);
                } else {
                    String message = getMessage(EMPLOYEE_DOES_NOT_EXIT, null);
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.addCommonError(message);
                    response = new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                response = getGeneralErrorResponse();
            }
        }
        return response;
    }

    private AuthorizationResponseModel buildResponseModel(Employee employee){
        AuthorizationResponseModel responseModel = new AuthorizationResponseModel();
        responseModel.setId(employee.getId());
        responseModel.setFirstName(employee.getFirstName());
        responseModel.setSecondName(employee.getSecondName());
        responseModel.setFatherName(employee.getFatherName());
        return responseModel;
    }
}
