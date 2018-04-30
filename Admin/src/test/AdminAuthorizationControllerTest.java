import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import ru.innopolis.controllers.AdminAuthorizationController;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IStaffDAOService;
import ru.innopolis.dao.entity.Staff;

import org.junit.*;
import org.powermock.api.mockito.PowerMockito;
import org.mockito.Mockito;
import ru.innopolis.models.AuthorizationRequestModel;
import ru.innopolis.models.AuthorizationResponseModel;
import ru.innopolis.models.ErrorResponse;

import javax.servlet.http.HttpSession;


import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Created by ananas on 29/04/18.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DAOServiceFactory.class, IStaffDAOService.class})
public class AdminAuthorizationControllerTest {


    private DAOServiceFactory dao_mock;
    private IStaffDAOService service_mock;

    @Before
    public void setUp() throws Exception
    {

        PowerMockito.mockStatic(DAOServiceFactory.class);
        dao_mock = PowerMockito.mock(DAOServiceFactory.class);
        PowerMockito.when(DAOServiceFactory.getInstance()).thenReturn(dao_mock);
        service_mock = PowerMockito.mock(IStaffDAOService.class);
        PowerMockito.when(dao_mock, "createService", IStaffDAOService.class).thenReturn(service_mock);

    }


    @Test
    public void authenticateExistingUser() throws Exception {

        Staff staff = new Staff();
        PowerMockito.when(service_mock.findStaff(Mockito.anyString(), Mockito.anyString())).thenReturn(staff);


        HttpSession session_mock = PowerMockito.mock(HttpSession.class);

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);

        AdminAuthorizationController tested_controller = new AdminAuthorizationController(messageSource);
        AuthorizationRequestModel model = new AuthorizationRequestModel();
        model.setPassword("123456");
        model.setMail("m@m.ru");
        ResponseEntity response = tested_controller.authenticate(model, errors_mock, session_mock);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(response.getBody(), instanceOf(AuthorizationResponseModel.class));
    }

    @Test
    public void authenticateNonexistingUser() throws Exception {

        PowerMockito.when(service_mock.findStaff(Mockito.anyString(), Mockito.anyString())).thenReturn(null);

        HttpSession session_mock = PowerMockito.mock(HttpSession.class);

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);

        AdminAuthorizationController tested_controller = new AdminAuthorizationController(messageSource);
        AuthorizationRequestModel model = new AuthorizationRequestModel();
        model.setPassword("123456");
        model.setMail("m@m.ru");
        ResponseEntity response = tested_controller.authenticate(model, errors_mock, session_mock);

        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody(), instanceOf(ErrorResponse.class));

    }

    @Test
    public void authenticateWithErrors() throws Exception {

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.TRUE);

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);

        AdminAuthorizationController tested_controller = new AdminAuthorizationController(messageSource);
        ResponseEntity response = tested_controller.authenticate(null, errors_mock, null);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertThat(response.getBody(), instanceOf(ErrorResponse.class));

    }

    @Test
    public void logOut() throws Exception {
        HttpSession session_mock = PowerMockito.mock(HttpSession.class);
        MessageSource messageSource = PowerMockito.mock(MessageSource.class);

        AdminAuthorizationController tested_controller = new AdminAuthorizationController(messageSource);
        ResponseEntity response = tested_controller.logOut(session_mock);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

    }

    @After
    public void tearDown() throws Exception {

        service_mock = null;
        dao_mock = null;
    }

}