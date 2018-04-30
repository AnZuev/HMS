package ru.innopolis.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import ru.innopolis.controllers.AdminHotelController;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IHotelDAOService;
import ru.innopolis.dao.IStaffDAOService;
import ru.innopolis.dao.entity.Hotel;
import ru.innopolis.models.CreateHotelModelRequest;
import java.util.LinkedList;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.instanceOf;

/**
 * Created by ananas on 30/04/18.
 */


@RunWith(PowerMockRunner.class)
@PrepareForTest({DAOServiceFactory.class, IStaffDAOService.class})
public class AdminHotelControllerTest {

    private DAOServiceFactory dao_mock;
    private IHotelDAOService service_mock;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(DAOServiceFactory.class);
        dao_mock = PowerMockito.mock(DAOServiceFactory.class);
        PowerMockito.when(DAOServiceFactory.getInstance()).thenReturn(dao_mock);
        service_mock = PowerMockito.mock(IHotelDAOService.class);
        PowerMockito.when(dao_mock, "createService", IHotelDAOService.class).thenReturn(service_mock);

    }

    @After
    public void tearDown() throws Exception {

        service_mock = null;
        dao_mock = null;

    }

    @Test
    public void createHotel() throws Exception {

        CreateHotelModelRequest model = new CreateHotelModelRequest();
        MessageSource messageSource = PowerMockito.mock(MessageSource.class);

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);

        AdminHotelController testedController = new AdminHotelController(messageSource);

        ResponseEntity response = testedController.createHotel(model, errors_mock);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

    }


    @Test
    public void getFullHotelInformationEmpty() throws Exception {

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);
        AdminHotelController testedController = new AdminHotelController(messageSource);
        PowerMockito.when(service_mock.getAllHotels()).thenReturn(new LinkedList<Hotel>());


        ResponseEntity response = testedController.getFullHotelInformation();
        assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
        assertThat(response.getBody(), instanceOf(LinkedList.class));


    }

    @Test
    public void getFullHotelInformationOne() throws Exception {

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);
        AdminHotelController testedController = new AdminHotelController(messageSource);


        LinkedList<Hotel> list = new LinkedList<>();
        Hotel h = new Hotel();
        h.setId((long) 0);
        h.setName("");

        list.add(h);
        PowerMockito.when(service_mock.getAllHotels()).thenReturn(list);


        ResponseEntity response = testedController.getFullHotelInformation();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(response.getBody(), instanceOf(LinkedList.class));

    }

}