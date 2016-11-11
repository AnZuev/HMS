package ru.innopolis.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.innopolis.constants.AuthorizationConstant;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IRoomDAOService;
import ru.innopolis.dao.entity.Employee;
import ru.innopolis.dao.entity.addition.ManagerOrderDescription;
import ru.innopolis.exceptions.UserException;
import ru.innopolis.models.OrderIDModelRequest;
import ru.innopolis.models.OrderListModelRequest;
import ru.innopolis.models.OrderModelResponse;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * Отмена заказа
     * @param model Модель запроса на оплату заказа
     * @param errors Список ошибок валидации
     * @param session Сессия
     * @return Код 200 - удалось отменить. В противном случае описание ошибки.
     */
    @PostMapping("/order/cancel")
    public ResponseEntity cancelOrder(@Valid @RequestBody OrderIDModelRequest model, Errors errors, HttpSession session){
        ResponseEntity response = getValidationErrorResponse(errors);
        if (response == null){
            Employee employee = (Employee) session.getAttribute(AuthorizationConstant.EMPLOYEE_KEY);
            try {
                IRoomDAOService service = DAOServiceFactory.getInstance().createService(IRoomDAOService.class);
                service.cancelOrder(model.getOrderId(), employee.getHotelId());
            }catch (UserException e) {
                response = handleUserException(e);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
                response = getGeneralErrorResponse();
            }
        }
        return response;
    }

    /**
     * Получить список заказов
     * @param modelRequest Модель запроса на получения списка заказов
     * @param errors Список ошибок валидации
     * @param session Сессия
     * @return Список заказов
     */
    @PostMapping("/orders")
    public ResponseEntity getOrders(@Valid @RequestBody OrderListModelRequest modelRequest, Errors errors, HttpSession session){
        ResponseEntity response = getValidationErrorResponse(errors);
        if (response == null){
            Employee employee = (Employee) session.getAttribute(AuthorizationConstant.EMPLOYEE_KEY);
            try {
                IRoomDAOService service = DAOServiceFactory.getInstance().createService(IRoomDAOService.class);
                List<ManagerOrderDescription> orders = service.getOrders(modelRequest.getStartDate(), modelRequest.getFinishDate(), employee.getHotelId());
                List<OrderModelResponse> responseModels = new ArrayList<>(orders.size());
                orders.forEach(order -> {
                    OrderModelResponse m = new OrderModelResponse();
                    BeanUtils.copyProperties(order, m);
                    m.setStatus(order.getStatus().name());
                    responseModels.add(m);
                });
                HttpStatus status = responseModels.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
                response = new ResponseEntity(responseModels, status);
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
