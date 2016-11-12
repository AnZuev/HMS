package ru.innopolis.controllers;

import org.springframework.beans.BeanUtils;
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
import ru.innopolis.dao.IClientDAOService;
import ru.innopolis.dao.entity.Client;
import ru.innopolis.exceptions.UserException;
import ru.innopolis.helpers.PasswordHelper;
import ru.innopolis.models.NewClientModel;
import ru.innopolis.models.RegistrationResponseModel;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Создано: Денис
 * Дата:  17.10.2016
 * Описание: Контролер для регистрации нового пользователя
 */
@RestController
public class RegistrationController extends BaseRestController {

    private Logger logger = Logger.getLogger(RegistrationController.class.getName());

    @Autowired
    public RegistrationController(MessageSource messageSource) {
        super(messageSource);
    }


    /**
     * Зарегистрировать нового пользователя в системе
     *
     * @param newUser       Новый пользователь
     * @param errors Результат валидации
     * @param request       Запрос от клиента
     * @return Ответ, содержащий статус действия
     */
    @PostMapping("/auth/signUp")
    public ResponseEntity createUser(@Valid @RequestBody NewClientModel newUser, Errors errors, HttpServletRequest request) {
        ResponseEntity responseEntity = getValidationErrorResponse(errors);
        if (responseEntity == null) {
            try {
                Client client = buildNewClient(newUser);
                IClientDAOService service = DAOServiceFactory.getInstance().createService(IClientDAOService.class);
                client = service.addNewClient(client);
                request.getSession().setAttribute(AuthorizationConstant.AUTHORIZATION_KEY, Boolean.TRUE);
                RegistrationResponseModel responseModel = buildResponseModel(client);
                responseEntity = new ResponseEntity<>(responseModel, HttpStatus.OK);
            } catch (UserException e) {
                responseEntity = handleUserException(e);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                responseEntity = getGeneralErrorResponse();
            }
        }
        return responseEntity;
    }

    private RegistrationResponseModel buildResponseModel(Client client) {
        RegistrationResponseModel model = new RegistrationResponseModel();
        BeanUtils.copyProperties(client, model);
        return model;
    }

    private Client buildNewClient(NewClientModel model) throws Exception {
        Client client = new Client();
        BeanUtils.copyProperties(model, client);
        String encryptPassword = PasswordHelper.encrypt(model.getPassword());
        client.setPassword(encryptPassword);
        return client;
    }
}
