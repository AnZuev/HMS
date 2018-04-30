import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IClientDAOService;

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

    }

}