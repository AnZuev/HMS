package ru.innopolis.controllers;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IHotelDAOService;
import ru.innopolis.dao.entity.Hotel;
import ru.innopolis.dao.entity.RoomType;
import ru.innopolis.dao.imp.HotelDAOService;
import ru.innopolis.models.ErrorResponse;
import ru.innopolis.models.HotelResponseModel;
import ru.innopolis.models.RoomTypeResponseModel;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Создано: Денис
 * Дата:  30.10.2016
 * Описание: Контролер по работе с отелями
 */
@RestController()
public class HotelController extends BaseRestController {

    private static final String HOTEL_ID_IS_NOT_CORRECT_MESSAGE = "hotel.id.does.not.exist";
    private Logger logger = Logger.getLogger(HotelController.class.getName());

    public HotelController(MessageSource messageSource) {
        super(messageSource);
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
            fillHotelInformation((hotel) -> {
                HotelResponseModel model = new HotelResponseModel();
                model.setId(hotel.getId());
                model.setName(hotel.getName());
                model.setAddress(hotel.getAddress());
                model.setDescription(hotel.getDescription());
                model.setMail(hotel.getMail());
                model.setPhoneNumber(hotel.getPhoneNumber());
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

    /**
     * Получить краткую информацию обо всех отелях
     * @return Краткая информацию обо всех отелях
     */
    @GetMapping("/hotels/short")
    public ResponseEntity getShortHotelInformation() {
        List<HotelResponseModel> output = new LinkedList<>();
        ResponseEntity response;
        try {
            fillHotelInformation((hotel) -> {
                HotelResponseModel model = new HotelResponseModel();
                model.setId(hotel.getId());
                model.setName(hotel.getName());
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

    /**
     * Заполнить информацию по отелям
     * @param consumer Правило заполнения данных
     */
    private void fillHotelInformation(Consumer<Hotel> consumer) throws Exception {
        IHotelDAOService service = DAOServiceFactory.getInstance().createService(HotelDAOService.class);
        List<Hotel> allHotels = service.getAllHotels();
        allHotels.forEach(consumer);
    }

    /**
     * Получить список типов комнат в рамках отеля
     * @param hotelId ИД отеля
     * @return Список типов комнат в рамках отеля
     */
    @GetMapping("/hotel/{hotelId}/rooms/getTypes/full")
    public ResponseEntity getRoomTypeInformation(@PathVariable String hotelId) {
        LinkedList<RoomTypeResponseModel> output = new LinkedList<>();
        ResponseEntity response = getRoomTypeInformation(hotelId, output, (type) ->{
            RoomTypeResponseModel model = new RoomTypeResponseModel();
            model.setId(type.getId());
            model.setName(type.getName());
            model.setDescription(type.getDescription());
            model.setCost(type.getCost());
            model.setPhotoPath(type.getPhotoPath());
            output.add(model);
        });
        return response;
    }

    /**
     * Получить список типов комнат в рамках отеля с урезанной информацией
     * @param hotelId ИД отеля
     * @return Список типов комнат в рамках отеля
     */
    @GetMapping("/hotel/{hotelId}/rooms/getTypes/short")
    public ResponseEntity getShortRoomTypeInformation(@PathVariable String hotelId) {
        LinkedList<RoomTypeResponseModel> output = new LinkedList<>();
        ResponseEntity response = getRoomTypeInformation(hotelId, output, (type) ->{
            RoomTypeResponseModel model = new RoomTypeResponseModel();
            model.setId(type.getId());
            model.setName(type.getName());
            output.add(model);
        });
        return response;
    }

    /**
     * Получить информацию о типах комнат в рамках отеля
     * @param hotelId ИД отеля
     * @param output Список, куда сохранять модель отеля
     * @param builder Строитель RoomTypeResponseModel
     * @return Ответ клиенту
     */
    private ResponseEntity getRoomTypeInformation(String hotelId, List<RoomTypeResponseModel> output, Consumer<RoomType> builder){
        ResponseEntity response;
        Long id = parse(hotelId);
        if (id != null){
            try {
                IHotelDAOService service = DAOServiceFactory.getInstance().createService(HotelDAOService.class);
                List<RoomType> list = service.getRoomTypesByHotelId(id);
                list.forEach(builder);
                HttpStatus status = output.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
                response = new ResponseEntity<>(output, status);
            }catch (Exception e){
                logger.log(Level.SEVERE, e.getMessage(), e);
                response = getGeneralErrorResponse();
            }
        }else {
            String message = getMessage(HOTEL_ID_IS_NOT_CORRECT_MESSAGE, null);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.addCommonError(message);
            response = new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * Распарсить ИД.
     * @param idAsString ИД ввиде строки
     * @return ИД в виде числа, если удалось распарсить. В противном случае null.
     */
    private Long parse(String idAsString){
        Long result = null;
        try {
            result = Long.parseLong(idAsString);
        }catch (Exception e){
            logger.log(Level.INFO, e.getMessage());
        }
        return result;
    }
}
