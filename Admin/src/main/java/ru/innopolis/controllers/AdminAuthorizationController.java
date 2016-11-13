package ru.innopolis.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.innopolis.constants.AuthorizationConstant;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IStaffDAOService;
import ru.innopolis.dao.entity.Staff;
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
public class AdminAuthorizationController extends BaseRestController {

    private static final String STAFF_DOES_NOT_EXIT_MESSAGE = "staff.does.not.exit";
    private Logger logger = Logger.getLogger(AdminAuthorizationController.class.getName());

    @Autowired
    public AdminAuthorizationController(MessageSource messageSource) {
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

                IStaffDAOService service = DAOServiceFactory.getInstance().createService(IStaffDAOService.class);
                Staff staff = service.findStaff(model.getMail(), encryptPassword);
                if (staff != null) {
                    session.setAttribute(AuthorizationConstant.AUTHORIZATION_KEY, Boolean.TRUE);
                    session.setAttribute(AuthorizationConstant.STAFF_KEY, staff);
                    AuthorizationResponseModel responseModel = buildResponseModel(staff);
                    response = new ResponseEntity<>(responseModel, HttpStatus.OK);
                } else {
                    String message = getMessage(STAFF_DOES_NOT_EXIT_MESSAGE, null);
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

    private AuthorizationResponseModel buildResponseModel(Staff staff) {
        AuthorizationResponseModel responseModel = new AuthorizationResponseModel();
        BeanUtils.copyProperties(staff, responseModel);
        return responseModel;
    }

    @GetMapping("/auth/logout")
    public ResponseEntity logOut(HttpSession session){
        session.invalidate();
        return new ResponseEntity(HttpStatus.OK);
    }
}
