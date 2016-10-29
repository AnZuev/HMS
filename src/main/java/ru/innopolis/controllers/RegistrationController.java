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
import ru.innopolis.dao.IClientDAOService;
import ru.innopolis.dao.imp.ClientDAOService;
import ru.innopolis.models.NewClientModel;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Создано: Денис
 * Дата:  17.10.2016
 * Описание: Контролер для регистрации нового пользователя
 */
@RestController
public class RegistrationController extends BaseRestController{

    private Logger logger = Logger.getLogger(RegistrationController.class.getName());

    @Autowired
    public RegistrationController(MessageSource messageSource) {
       super(messageSource);
    }


    /**
     * Зарегистрировать нового пользователя в системе
     * @param newUser Новый пользователь
     * @param bindingResult Результат валидации
     * @param request Запрос от клиента
     * @return Ответ, содержащий статус действия
     */
    @PostMapping("/registration")
    public ResponseEntity createUser(@Valid @RequestBody NewClientModel newUser, Errors bindingResult, HttpServletRequest request){
        ResponseEntity responseEntity = getValidationErrorResponse(bindingResult);
        if (responseEntity == null)
        {
            IClientDAOService service = new ClientDAOService();
            try {
                service.addNewClient(newUser);
                request.getSession().setAttribute(AuthorizationConstant.AUTHORIZATION_KEY, Boolean.TRUE);
                responseEntity = new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                responseEntity = getGeneralErrorResponse();
            }
        }

        return responseEntity;
    }
}
