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
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IEmployeeDAOService;
import ru.innopolis.dao.entity.Employee;
import ru.innopolis.models.CreateEditEmployeeModelRequest;
import ru.innopolis.models.DeleteEmployeeModelRequest;

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
@PrepareForTest({DAOServiceFactory.class, IEmployeeDAOService.class})

public class EmployeeControllerTest {


    private DAOServiceFactory dao_mock;
    private IEmployeeDAOService service_mock;
    HttpSession session_mock;

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
    public void createNewEmployee() throws Exception {

        CreateEditEmployeeModelRequest model = new CreateEditEmployeeModelRequest();
        model.setPassword("23456");
        model.setMail("a@a.ru");
        MessageSource messageSource = PowerMockito.mock(MessageSource.class);

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);

        EmployeeController testedController = new EmployeeController(messageSource);


        ResponseEntity response = testedController.createNewEmployee(model, errors_mock, session_mock);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void getAllEmpty() throws Exception {

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);


        LinkedList<Employee> list = new LinkedList<>();
        PowerMockito.when(service_mock.getManagersByHotelId(Mockito.anyLong()))
                .thenReturn(list);


        MessageSource messageSource = PowerMockito.mock(MessageSource.class);
        EmployeeController testedController = new EmployeeController(messageSource);


        ResponseEntity response = testedController.getAll(session_mock);

        assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
        assertThat(response.getBody(), instanceOf(ArrayList.class));

    }

    @Test
    public void getAllOne() throws Exception {

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);


        LinkedList<Employee> list = new LinkedList<>();
        Employee emp = new Employee();
        emp.setType(Employee.Type.OWNER);
        list.add(emp);
        PowerMockito.when(service_mock.getManagersByHotelId(Mockito.anyLong()))
                .thenReturn(list);


        MessageSource messageSource = PowerMockito.mock(MessageSource.class);
        EmployeeController testedController = new EmployeeController(messageSource);


        ResponseEntity response = testedController.getAll(session_mock);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(response.getBody(), instanceOf(ArrayList.class));

    }

    @Test
    public void deleteEmployee() throws Exception {

        DeleteEmployeeModelRequest model = new DeleteEmployeeModelRequest();
        model.setEmployeeId((long) 1);
        MessageSource messageSource = PowerMockito.mock(MessageSource.class);

        EmployeeController testedController = new EmployeeController(messageSource);

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);

        ResponseEntity response = testedController.deleteEmployee(model, errors_mock, session_mock);


        assertEquals(response.getStatusCode(), HttpStatus.OK);

    }


}