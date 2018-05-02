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
import ru.innopolis.controllers.OwnerController;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IEmployeeDAOService;
import ru.innopolis.dao.entity.addition.ExtendedEmployee;
import ru.innopolis.models.CreateEditHotelOwnerModelRequest;
import ru.innopolis.models.OwnerIdResponseModel;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.instanceOf;


/**
 * Created by ananas on 30/04/18.
 */


@RunWith(PowerMockRunner.class)
@PrepareForTest({DAOServiceFactory.class, IEmployeeDAOService.class})
public class OwnerControllerTest {

    private DAOServiceFactory dao_mock;
    private IEmployeeDAOService service_mock;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(DAOServiceFactory.class);
        dao_mock = PowerMockito.mock(DAOServiceFactory.class);
        PowerMockito.when(DAOServiceFactory.getInstance()).thenReturn(dao_mock);
        service_mock = PowerMockito.mock(IEmployeeDAOService.class);
        PowerMockito.when(dao_mock, "createService", IEmployeeDAOService.class).thenReturn(service_mock);

    }

    @After
    public void tearDown() throws Exception {

        service_mock = null;
        dao_mock = null;

    }

    @Test
    public void createOrUpdateNull() throws Exception {

        CreateEditHotelOwnerModelRequest model = PowerMockito.mock(CreateEditHotelOwnerModelRequest.class);
        PowerMockito.when(model.getId()).thenReturn(null);

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);

        OwnerController testedController = new OwnerController(messageSource);
        ResponseEntity response = testedController.createOrUpdate(model, errors_mock);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(response.getBody(), instanceOf(OwnerIdResponseModel.class));

    }

    @Test
    public void createOrUpdateNotNull() throws Exception {

        CreateEditHotelOwnerModelRequest model = PowerMockito.mock(CreateEditHotelOwnerModelRequest.class);
        PowerMockito.when(model.getId()).thenReturn((long)0);

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);

        OwnerController testedController = new OwnerController(messageSource);
        ResponseEntity response = testedController.createOrUpdate(model, errors_mock);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNull(response.getBody());

    }

    @Test
    public void getListOfOwnersEmpty() throws Exception {

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);
        PowerMockito.when(service_mock.getOwners()).thenReturn(new LinkedList<ExtendedEmployee>());

        OwnerController testedController = new OwnerController(messageSource);

        ResponseEntity response = testedController.getListOfOwners();
        assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
        assertThat(response.getBody(), instanceOf(ArrayList.class));

    }


    @Test
    public void getListOfOwnersOne() throws Exception {

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);



        LinkedList<ExtendedEmployee> list = new LinkedList<>();
        ExtendedEmployee ee = new ExtendedEmployee();
        ee.setId((long) 0);
        list.add(ee);

        PowerMockito.when(service_mock.getOwners()).thenReturn(list);

        OwnerController testedController = new OwnerController(messageSource);

        ResponseEntity response = testedController.getListOfOwners();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertThat(response.getBody(), instanceOf(ArrayList.class));

    }

}