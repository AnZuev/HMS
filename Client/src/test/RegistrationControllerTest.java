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
import ru.innopolis.controllers.ClientController;
import ru.innopolis.controllers.RegistrationController;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IClientDAOService;
import ru.innopolis.dao.entity.Client;
import ru.innopolis.models.NewClientModel;

import javax.servlet.http.HttpSession;

import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 * Created by ananas on 30/04/18.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({DAOServiceFactory.class, IClientDAOService.class})
public class RegistrationControllerTest {

    private DAOServiceFactory dao_mock;
    private IClientDAOService service_mock;

    @Before
    public void setUp() throws Exception
    {

        PowerMockito.mockStatic(DAOServiceFactory.class);
        dao_mock = PowerMockito.mock(DAOServiceFactory.class);
        PowerMockito.when(DAOServiceFactory.getInstance()).thenReturn(dao_mock);
        service_mock = PowerMockito.mock(IClientDAOService.class);
        PowerMockito.when(dao_mock, "createService", IClientDAOService.class).thenReturn(service_mock);

    }

    @After
    public void tearDown() throws Exception {

        service_mock = null;
        dao_mock = null;
    }


    @Test
    public void createUser() throws Exception {

        HttpSession session_mock = PowerMockito.mock(HttpSession.class);
        Client client = new Client();


//        PowerMockito.when(session_mock.getAttribute(AuthorizationConstant.CLIENT_KEY)).thenReturn(client);

        Errors errors_mock = PowerMockito.mock(Errors.class);
        PowerMockito.when(errors_mock.hasErrors()).thenReturn(Boolean.FALSE);

        MessageSource messageSource = PowerMockito.mock(MessageSource.class);

        NewClientModel model = new NewClientModel();
        model.setPassword("1234567");
        model.setMail("a@a.ru");


        PowerMockito.when(service_mock.addNewClient(Mockito.anyObject())).thenReturn(client);

        RegistrationController testedController = new RegistrationController(messageSource);
        ResponseEntity response = testedController.createUser(model, errors_mock, session_mock);

        assertEquals(response.getStatusCode(), HttpStatus.OK);

    }

}