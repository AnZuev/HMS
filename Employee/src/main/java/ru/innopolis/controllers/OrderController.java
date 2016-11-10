package ru.innopolis.controllers;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.innopolis.constants.AuthorizationConstant;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IRoomDAOService;
import ru.innopolis.dao.entity.Employee;
import ru.innopolis.exceptions.UserException;
import ru.innopolis.models.OrderIDModelRequest;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Создано: Денис
 * Дата:  10.11.2016
 * Описание: Контролер по работе с заказами
 */
@RestController
@RequestMapping("/private")
public class OrderController extends BaseRestController{

    private Logger logger = java.util.logging.Logger.getLogger(OrderController.class.getName());

    public OrderController(MessageSource messageSource) {
        super(messageSource);
    }

    /**
     * Оплатить заказ
     * @param model Модель запроса на оплату заказа
     * @param errors Список ошибок валидации
     * @param session Сессия
     * @return Код 200 - удалось оплатить. В противном случае описание ошибки.
     */
    @PostMapping("/order/pay")
    public ResponseEntity payOrders(@Valid @RequestBody OrderIDModelRequest model, Errors errors, HttpSession session){
        ResponseEntity response = getValidationErrorResponse(errors);
        if (response == null){
            Employee employee = (Employee) session.getAttribute(AuthorizationConstant.EMPLOYEE_KEY);
            try {
                IRoomDAOService service = DAOServiceFactory.getInstance().createService(IRoomDAOService.class);
                service.payRoom(model.getOrderId(), employee.getHotelId());
            }catch (UserException e) {
                response = handleUserException(e);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                response = getGeneralErrorResponse();
            }
        }
        return response;
    }
}
