import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
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
import ru.innopolis.controllers.EmployeeAuthorizationController;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IEmployeeDAOService;
import ru.innopolis.dao.entity.Employee;
import ru.innopolis.models.AuthorizationRequestModel;
import ru.innopolis.models.AuthorizationResponseModel;
import ru.innopolis.models.ErrorResponse;

import javax.servlet.http.HttpSession;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by ananas on 01/05/18.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({DAOServiceFactory.class, IEmployeeDAOService.class})
public class EmployeeAuthorizationControllerTest {


    private DAOServiceFactory dao_mock;
    private IEmployeeDAOService service_mock;
    private HttpSession session_mock;

    @Before
    public void setUp() throws Exception
    {

        PowerMockito.mockStatic(DAOServiceFactory.class);
        dao_mock = PowerMockito.mock(DAOServiceFactory.class);
        PowerMockito.when(DAOServiceFactory.getInstance()).thenReturn(dao_mock);
        service_mock = PowerMockito.mock(IEmployeeDAOService.class);
        PowerMockito.when(dao_mock, "createService", IEmployeeDAOService.class).thenReturn(service_mock);


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
    @Description("Login admin existing user without any errors")
    @DisplayName("Human")
    public void authenticateExistingUser() throws Exception {

        Employee emp = new Employee();
        emp.setType(Employee.Type.OWNER);
        PowerMockito.when(service_mock.findEmployee(Mockito.anyString(), Mockito.anyString())).thenReturn(emp);


        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);

        EmployeeAuthorizationController tested_controller = new EmployeeAuthorizationController(messageSource);
        AuthorizationRequestModel model = new AuthorizationRequestModel();
        model.setPassword("123456");
        model.setMail("m@m.ru");
        ResponseEntity response = tested_controller.authenticate(model, errors_mock, session_mock);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(response.getBody(), instanceOf(AuthorizationResponseModel.class));
    }

    @Test
    public void authenticateNonexistingUser() throws Exception {

        PowerMockito.when(service_mock.findEmployee(Mockito.anyString(), Mockito.anyString())).thenReturn(null);

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);

        EmployeeAuthorizationController tested_controller = new EmployeeAuthorizationController(messageSource);
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

        EmployeeAuthorizationController tested_controller = new EmployeeAuthorizationController(messageSource);
        ResponseEntity response = tested_controller.authenticate(null, errors_mock, null);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertThat(response.getBody(), instanceOf(ErrorResponse.class));

    }

    @Test
    public void logOut() throws Exception {
        MessageSource messageSource = PowerMockito.mock(MessageSource.class);

        EmployeeAuthorizationController tested_controller = new EmployeeAuthorizationController(messageSource);
        ResponseEntity response = tested_controller.logOut(session_mock);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

    }


}
