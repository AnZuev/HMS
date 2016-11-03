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
import ru.innopolis.dao.IClientDAOService;
import ru.innopolis.dao.entity.Client;
import ru.innopolis.dao.imp.ClientDAOService;
import ru.innopolis.helpers.PasswordHelper;
import ru.innopolis.models.NewClientModel;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Создано: Денис
 * Дата:  04.11.2016
 * Описание: Контроллер по работе с клиентом
 */
@RestController
public class ClientController extends BaseRestController {

    private Logger logger = Logger.getLogger(ClientController.class.getName());

    @Autowired
    public ClientController(MessageSource messageSource) {
        super(messageSource);
    }

    /**
     * Изменить данные профиля
     * @param requestModel Запрос на изменения данных
     * @param errors Список ошибок валидации
     * @param session Сессия
     * @return 200 код если всё ОК, в противном случае 500 код.
     */
    @PostMapping("/private/profile/edit")
    public ResponseEntity editProfil(@Valid @RequestBody NewClientModel requestModel, Errors errors, HttpSession session) {
        ResponseEntity responseEntity = getValidationErrorResponse(errors);
        if (responseEntity == null) {
            try {
                Client oldClientData = (Client) session.getAttribute(AuthorizationConstant.CLIENT_KEY);
                Client newClientData = buildNewClient(requestModel);
                newClientData.setId(oldClientData.getId());

                IClientDAOService service = DAOServiceFactory.getInstance().createService(ClientDAOService.class);
                service.updateClientInformation(newClientData);

                session.setAttribute(AuthorizationConstant.CLIENT_KEY, newClientData);
                responseEntity = new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                responseEntity = getGeneralErrorResponse();
            }
        }

        return responseEntity;
    }


    private Client buildNewClient(NewClientModel model) throws Exception {
        String encryptPassword = PasswordHelper.encrypt(model.getPassword());
        Client client = new Client();
        client.setPassword(encryptPassword);
        client.setFirstName(model.getFirstName());
        client.setSecondName(model.getSecondName());
        client.setFatherName(model.getFatherName());
        client.setMail(model.getEmail());
        client.setPhoneNumber(model.getPhoneNumber());
        return client;
    }
}
