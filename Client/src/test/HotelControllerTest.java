import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IClientDAOService;
import ru.innopolis.dao.IHotelDAOService;
import ru.innopolis.dao.entity.Hotel;
import ru.innopolis.controllers.*;
import ru.innopolis.dao.entity.RoomType;

import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Created by ananas on 30/04/18.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({DAOServiceFactory.class, IHotelDAOService.class})
public class HotelControllerTest {


    private DAOServiceFactory dao_mock;
    private IHotelDAOService service_mock;

    @Before
    public void setUp() throws Exception
    {

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
    public void getFullHotelInformationEmpty() throws Exception {

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);
        HotelController testedController = new HotelController(messageSource);
        PowerMockito.when(service_mock.getAllHotels()).thenReturn(new LinkedList<Hotel>());


        ResponseEntity response = testedController.getFullHotelInformation();
        assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
        assertThat(response.getBody(), instanceOf(LinkedList.class));


    }

    @Test
    public void getFullHotelInformationOne() throws Exception {

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);
        HotelController testedController = new HotelController(messageSource);


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

    @Test
    public void getShortHotelInformationEmpty() throws Exception {

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);
        HotelController testedController = new HotelController(messageSource);
        PowerMockito.when(service_mock.getAllHotels()).thenReturn(new LinkedList<Hotel>());


        ResponseEntity response = testedController.getShortHotelInformation();
        assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
        assertThat(response.getBody(), instanceOf(LinkedList.class));


    }

    @Test
    public void getShortHotelInformationOne() throws Exception {

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);
        HotelController testedController = new HotelController(messageSource);


        LinkedList<Hotel> list = new LinkedList<>();
        Hotel h = new Hotel();
        h.setId((long) 0);
        h.setName("");

        list.add(h);
        PowerMockito.when(service_mock.getAllHotels()).thenReturn(list);


        ResponseEntity response = testedController.getShortHotelInformation();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(response.getBody(), instanceOf(LinkedList.class));

    }

    @Test
    public void getRoomTypeInformationEmpty() throws Exception {

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);

        HotelController testedController = new HotelController(messageSource);

        PowerMockito.when(service_mock.getRoomTypesByHotelId(Mockito.anyLong())).thenReturn(new LinkedList<RoomType>());


        ResponseEntity response = testedController.getRoomTypeInformation("3");
        assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
        assertThat(response.getBody(), instanceOf(LinkedList.class));


    }

    @Test
    public void getRoomTypeInformationOne() throws Exception {

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);
        HotelController testedController = new HotelController(messageSource);


        LinkedList<Hotel> list = new LinkedList<>();
        Hotel h = new Hotel();
        h.setId((long) 0);
        h.setName("");

        list.add(h);
        PowerMockito.when(service_mock.getAllHotels()).thenReturn(list);


        ResponseEntity response = testedController.getShortHotelInformation();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(response.getBody(), instanceOf(LinkedList.class));

    }

    @Test
    public void getRoomTypeInformation() throws Exception {

    }

    @Test
    public void getShortRoomTypeInformation() throws Exception {

    }

}