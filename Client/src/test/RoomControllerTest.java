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
import org.springframework.validation.Errors;
import ru.innopolis.controllers.RoomController;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IClientDAOService;
import ru.innopolis.dao.IRoomDAOService;
import ru.innopolis.dao.entity.addition.ExtendedRoom;
import ru.innopolis.models.AvailableRoomRequestModel;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Created by ananas on 01/05/18.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({DAOServiceFactory.class, IRoomDAOService.class})
public class RoomControllerTest {
    private DAOServiceFactory dao_mock;
    private IRoomDAOService service_mock;

    @Before
    public void setUp() throws Exception
    {

        PowerMockito.mockStatic(DAOServiceFactory.class);
        dao_mock = PowerMockito.mock(DAOServiceFactory.class);
        PowerMockito.when(DAOServiceFactory.getInstance()).thenReturn(dao_mock);
        service_mock = PowerMockito.mock(IRoomDAOService.class);
        PowerMockito.when(dao_mock, "createService", IRoomDAOService.class).thenReturn(service_mock);

    }

    @After
    public void tearDown() throws Exception {

        service_mock = null;
        dao_mock = null;
    }

    @Test
    public void findFreeRoomsEmpty() throws Exception {

        AvailableRoomRequestModel model = new AvailableRoomRequestModel();

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);


        LinkedList<ExtendedRoom> list = new LinkedList<>();
        PowerMockito.when(service_mock.getFreeRoomsInHotel(Mockito.anyLong(),Mockito.anyLong(), Mockito.anyObject(), Mockito.anyObject()))
                .thenReturn(list);


        MessageSource messageSource = PowerMockito.mock(MessageSource.class);
        RoomController testedController = new RoomController(messageSource);

        ResponseEntity response = testedController.findFreeRooms(model, errors_mock);

        assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
        assertThat(response.getBody(), instanceOf(ArrayList.class));

    }

    @Test
    public void findFreeRoomsOne() throws Exception {

        AvailableRoomRequestModel model = new AvailableRoomRequestModel();

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);


        LinkedList<ExtendedRoom> list = new LinkedList<>();
        ExtendedRoom room = new ExtendedRoom();
        room.setRoomNumber((long )433);
        room.setId((long) 1);
        room.setCost((double) 123);
        list.add(room);

        PowerMockito.when(service_mock.getFreeRoomsInHotel(Mockito.anyLong(),Mockito.anyLong(), Mockito.anyObject(), Mockito.anyObject()))
                .thenReturn(list);


        MessageSource messageSource = PowerMockito.mock(MessageSource.class);
        RoomController testedController = new RoomController(messageSource);

        ResponseEntity response = testedController.findFreeRooms(model, errors_mock);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(response.getBody(), instanceOf(ArrayList.class));

    }

    @Test
    public void bookRoom() throws Exception {

    }

    @Test
    public void cancelBook() throws Exception {

    }

    @Test
    public void getAllOrders() throws Exception {

    }

}