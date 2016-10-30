package ru.innopolis.controllers;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.innopolis.dao.IHotelDAOService;
import ru.innopolis.dao.entity.Hotel;
import ru.innopolis.dao.imp.HotelDAOService;
import ru.innopolis.models.HotelResponseModel;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Создано: Денис
 * Дата:  30.10.2016
 * Описание: Контролер по работе с отелями
 */
@RestController()
public class HotelController extends BaseRestController {

    public HotelController(MessageSource messageSource) {
        super(messageSource);
    }

    /**
     * Получить подробное описание всех отелей
     * @return Подробное описание всех отелей
     */
    @GetMapping("/hotels/full")
    public ResponseEntity<List<HotelResponseModel>> getFullHotelInformation() {
        List<HotelResponseModel> output = new LinkedList<>();
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
        ResponseEntity<List<HotelResponseModel>> response = new ResponseEntity<>(output, status);
        return response;
    }

    /**
     * Получить краткую информацию обо всех отелях
     * @return Краткая информацию обо всех отелях
     */
    @GetMapping("/hotels/short")
    public ResponseEntity<List<HotelResponseModel>> getShortHotelInformation() {
        List<HotelResponseModel> output = new LinkedList<>();
        fillHotelInformation((hotel) -> {
            HotelResponseModel model = new HotelResponseModel();
            model.setId(hotel.getId());
            model.setName(hotel.getName());
            output.add(model);
        });
        HttpStatus status = output.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        ResponseEntity<List<HotelResponseModel>> response = new ResponseEntity<>(output, status);
        return response;
    }

    /**
     * Заполнить информацию по отелям
     * @param consumer Правило заполнения данных
     */
    private void fillHotelInformation(Consumer<Hotel> consumer) {
        IHotelDAOService service = new HotelDAOService();
        List<Hotel> allHotels = service.getAllHotels();
        allHotels.forEach(consumer);
    }
}
