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
import ru.innopolis.constants.AuthorizationConstant;
import ru.innopolis.controllers.EmployeeController;
import ru.innopolis.controllers.EmployeeRoomController;
import ru.innopolis.controllers.RoomTypeController;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IHotelDAOService;
import ru.innopolis.dao.entity.Employee;
import ru.innopolis.dao.entity.RoomType;
import ru.innopolis.models.DeleteRoomTypeModelRequest;
import ru.innopolis.models.RoomTypeModelRequest;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by ananas on 01/05/18.
 */


@RunWith(PowerMockRunner.class)
@PrepareForTest({DAOServiceFactory.class, IHotelDAOService.class})

public class RoomTypeControllerTest {


    private DAOServiceFactory dao_mock;
    private IHotelDAOService service_mock;
    HttpSession session_mock;

    @Before
    public void setUp() throws Exception
    {

        PowerMockito.mockStatic(DAOServiceFactory.class);
        dao_mock = PowerMockito.mock(DAOServiceFactory.class);
        PowerMockito.when(DAOServiceFactory.getInstance()).thenReturn(dao_mock);
        service_mock = PowerMockito.mock(IHotelDAOService.class);
        PowerMockito.when(dao_mock, "createService", IHotelDAOService.class).thenReturn(service_mock);

        Employee employee = new Employee();
        employee.setType(Employee.Type.OWNER);
        employee.setId((long) 0);
        employee.setHotelId((long) 0);

        session_mock = PowerMockito.mock(HttpSession.class);
        PowerMockito.when(session_mock.getAttribute(AuthorizationConstant.EMPLOYEE_KEY)).thenReturn(employee);

    }

    @After
    public void tearDown() throws Exception {

        service_mock = null;
        dao_mock = null;
    }

    @Test
    public void createOrUpdate() throws Exception {

        RoomTypeModelRequest model = new RoomTypeModelRequest();


        MessageSource messageSource = PowerMockito.mock(MessageSource.class);

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);

        RoomTypeController testedController = new RoomTypeController(messageSource);
        ResponseEntity response = testedController.createOrUpdate(model, errors_mock, session_mock);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void delete() throws Exception {

        DeleteRoomTypeModelRequest model = new DeleteRoomTypeModelRequest();
        model.setRoomTypeId((long) 0);

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);


        RoomTypeController testedController = new RoomTypeController(messageSource);
        ResponseEntity response = testedController.delete(model, errors_mock, session_mock);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void getAllEmpty() throws Exception {

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);


        LinkedList<RoomType> list = new LinkedList<>();
        PowerMockito.when(service_mock.getRoomTypesByHotelId(Mockito.anyLong()))
                .thenReturn(list);


        MessageSource messageSource = PowerMockito.mock(MessageSource.class);

        RoomTypeController testedController = new RoomTypeController(messageSource);

        ResponseEntity response = testedController.getAll(session_mock);

        assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
        assertThat(response.getBody(), instanceOf(ArrayList.class));

    }

    @Test
    public void getAllOne() throws Exception {

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);

        LinkedList<RoomType> list = new LinkedList<>();
        RoomType roomType = new RoomType();
        roomType.setId((long) 0);
        list.add(roomType);


        PowerMockito.when(service_mock.getRoomTypesByHotelId(Mockito.anyLong()))
                .thenReturn(list);


        MessageSource messageSource = PowerMockito.mock(MessageSource.class);

        RoomTypeController testedController = new RoomTypeController(messageSource);
        ResponseEntity response = testedController.getAll(session_mock);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(response.getBody(), instanceOf(ArrayList.class));

    }

}