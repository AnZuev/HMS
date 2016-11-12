package ru.innopolis.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.innopolis.constants.AuthorizationConstant;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IHotelDAOService;
import ru.innopolis.dao.entity.Employee;
import ru.innopolis.dao.entity.Hotel;
import ru.innopolis.models.EditHotelModelRequest;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Создано: Денис
 * Дата:  10.11.2016
 * Описание: Контролер по работе с отелем
 */
@RestController
@RequestMapping("/private/owner")
public class HotelOwnerController extends BaseRestController {

    private Logger logger = java.util.logging.Logger.getLogger(HotelOwnerController.class.getName());

    public HotelOwnerController(MessageSource messageSource) {
        super(messageSource);
    }

    /**
     * Создать новый отель
     * @param modelRequest Модель нового отеля
     * @param errors Ошибки валидации
     * @return 200 если всё ок. Впротивном случае код ошибки и описание.
     */
    @PostMapping("/hotel/update")
    public ResponseEntity createHotel(@Valid @RequestBody EditHotelModelRequest modelRequest, Errors errors, HttpSession session){
        ResponseEntity response = getValidationErrorResponse(errors);
        if (response == null){
            Hotel hotel = convertToHotel(modelRequest);
            try {
                Employee owner = (Employee) session.getAttribute(AuthorizationConstant.EMPLOYEE_KEY);
                hotel.setId(owner.getHotelId());
                IHotelDAOService service = DAOServiceFactory.getInstance().createService(IHotelDAOService.class);
                service.updateHotel(hotel);
                response = new ResponseEntity(HttpStatus.OK);
            }catch (Exception e){
                logger.log(Level.SEVERE, e.getMessage(), e);
                response = getGeneralErrorResponse();
            }
        }
        return response;
    }

    private Hotel convertToHotel(EditHotelModelRequest model){
        Hotel h = new Hotel();
        BeanUtils.copyProperties(model, h);
        h.setName(model.getTitle());
        return h;
    }

}
