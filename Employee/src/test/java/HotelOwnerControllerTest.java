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
import ru.innopolis.controllers.HotelOwnerController;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IHotelDAOService;
import ru.innopolis.dao.entity.Employee;
import ru.innopolis.dao.entity.Hotel;
import ru.innopolis.models.EditHotelModelRequest;
import ru.innopolis.models.HotelModelResponse;

import javax.servlet.http.HttpSession;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by ananas on 01/05/18.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DAOServiceFactory.class, IHotelDAOService.class})

public class HotelOwnerControllerTest {


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
    public void createHotel() throws Exception {
        EditHotelModelRequest model = new EditHotelModelRequest();

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);


        HotelOwnerController testedController = new HotelOwnerController(messageSource);
        ResponseEntity response = testedController.createHotel(model, errors_mock, session_mock);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }



    @Test
    public void getHotelInformation() throws Exception {

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);

        Hotel hotel = new Hotel();
        PowerMockito.when(service_mock.getHotelInformation(Mockito.anyLong())).thenReturn(hotel);

        HotelOwnerController testedController = new HotelOwnerController(messageSource);
        ResponseEntity response = testedController.getHotelInformation(session_mock);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(response.getBody(), instanceOf(HotelModelResponse.class));

    }

}