package ru.innopolis.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IHotelDAOService;
import ru.innopolis.dao.entity.Hotel;
import ru.innopolis.models.CreateHotelModelRequest;
import ru.innopolis.models.HotelResponseModel;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Создано: Денис
 * Дата:  07.11.2016
 * Описание: Контроллер по работе с отелями
 */
@RestController
@RequestMapping("/private")
public class AdminHotelController extends BaseRestController {

    private Logger logger = java.util.logging.Logger.getLogger(OwnerController.class.getName());

    @Autowired
    public AdminHotelController(MessageSource messageSource) {
        super(messageSource);
    }

    /**
     * Создать новый отель
     * @param modelRequest Модель нового отеля
     * @param errors Ошибки валидации
     * @return 200 если всё ок. Впротивном случае код ошибки и описание.
     */
    @PutMapping("/hotel/new")
    public ResponseEntity createHotel(@Valid @RequestBody CreateHotelModelRequest modelRequest, Errors errors){
        ResponseEntity response = getValidationErrorResponse(errors);
        if (response == null){
            Hotel hotel = convertToHotel(modelRequest);
            try {
                IHotelDAOService service = DAOServiceFactory.getInstance().createService(IHotelDAOService.class);
                service.createHotel(hotel);
                response = new ResponseEntity(HttpStatus.OK);
            }catch (Exception e){
                logger.log(Level.SEVERE, e.getMessage(), e);
                response = getGeneralErrorResponse();
            }
        }
        return response;
    }

    private Hotel convertToHotel(CreateHotelModelRequest model){
        Hotel h = new Hotel();
        BeanUtils.copyProperties(model, h);
        return h;
    }


    /**
     * Получить подробное описание всех отелей
     * @return Подробное описание всех отелей
     */
    @GetMapping("/hotels/full")
    public ResponseEntity getFullHotelInformation() {
        List<HotelResponseModel> output = new LinkedList<>();
        ResponseEntity response;
        try {
            IHotelDAOService service = DAOServiceFactory.getInstance().createService(IHotelDAOService.class);
            List<Hotel> allHotels = service.getAllHotels();
            allHotels.forEach((hotel) -> {
                HotelResponseModel model = new HotelResponseModel();
                BeanUtils.copyProperties(hotel, model);
                output.add(model);
            });
            HttpStatus status = output.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
            response = new ResponseEntity<>(output, status);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            response = getGeneralErrorResponse();
        }
        return response;
    }
}
