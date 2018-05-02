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
import ru.innopolis.controllers.OrderController;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IRoomDAOService;
import ru.innopolis.dao.entity.Employee;
import ru.innopolis.dao.entity.OrderStatus;
import ru.innopolis.dao.entity.addition.ManagerOrderDescription;
import ru.innopolis.models.OrderIDModelRequest;
import ru.innopolis.models.OrderListModelRequest;

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
@PrepareForTest({DAOServiceFactory.class, IRoomDAOService.class})

public class OrderControllerTest {


    private DAOServiceFactory dao_mock;
    private IRoomDAOService service_mock;
    HttpSession session_mock;

    @Before
    public void setUp() throws Exception
    {

        PowerMockito.mockStatic(DAOServiceFactory.class);
        dao_mock = PowerMockito.mock(DAOServiceFactory.class);
        PowerMockito.when(DAOServiceFactory.getInstance()).thenReturn(dao_mock);
        service_mock = PowerMockito.mock(IRoomDAOService.class);
        PowerMockito.when(dao_mock, "createService", IRoomDAOService.class).thenReturn(service_mock);

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
    public void payOrders() throws Exception {


        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);

//        PowerMockito.when(service_mock.payRoom(Mockito.anyLong());)

        OrderIDModelRequest model = new OrderIDModelRequest();
        MessageSource messageSource = PowerMockito.mock(MessageSource.class);
        OrderController testedController = new OrderController(messageSource);
        model.setOrderId((long) 0);
        ResponseEntity response = testedController.payOrders(model, errors_mock, session_mock);


        assertEquals(response.getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void cancelOrder() throws Exception {


        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);
        OrderController testedController = new OrderController(messageSource);
        OrderIDModelRequest model = new OrderIDModelRequest();
        model.setOrderId((long) 0);
        ResponseEntity response = testedController.cancelOrder(model, errors_mock, session_mock);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void getOrdersEmpty() throws Exception {


        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);


        LinkedList<ManagerOrderDescription> list = new LinkedList<>();
        PowerMockito.when(service_mock.getOrders(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyLong()))
                .thenReturn(list);

        OrderListModelRequest model_mock = PowerMockito.mock(OrderListModelRequest.class);
        MessageSource messageSource = PowerMockito.mock(MessageSource.class);
        OrderController testedController = new OrderController(messageSource);
        ResponseEntity response = testedController.getOrders(model_mock, errors_mock, session_mock);


        assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
        assertThat(response.getBody(), instanceOf(ArrayList.class));

    }

    @Test
    public void getOrdersOne() throws Exception {

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);


        LinkedList<ManagerOrderDescription> list = new LinkedList<>();
        ManagerOrderDescription managerOrderDescription = new ManagerOrderDescription();
        managerOrderDescription.setStatus(OrderStatus.BOOKED);
        list.add(managerOrderDescription);
        PowerMockito.when(service_mock.getOrders(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyLong()))
                .thenReturn(list);

        OrderListModelRequest model_mock = PowerMockito.mock(OrderListModelRequest.class);
        MessageSource messageSource = PowerMockito.mock(MessageSource.class);
        OrderController testedController = new OrderController(messageSource);
        ResponseEntity response = testedController.getOrders(model_mock, errors_mock, session_mock);


        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(response.getBody(), instanceOf(ArrayList.class));

    }

}