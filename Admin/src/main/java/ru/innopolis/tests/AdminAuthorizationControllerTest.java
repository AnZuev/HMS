package ru.innopolis.tests;
import org.easymock.EasyMock;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import ru.innopolis.controllers.AdminAuthorizationController;
import ru.innopolis.dao.DAOServiceFactory;
import ru.innopolis.dao.IDAOServiceFactory;
import ru.innopolis.dao.IStaffDAOService;
import ru.innopolis.dao.entity.Staff;
import ru.innopolis.controllers.BaseRestController;

import org.junit.*;
import org.mockito.Mockito.*;
import org.powermock.api.easymock.PowerMock.*;
import org.easymock.EasyMock.*;
import ru.innopolis.models.AuthorizationRequestModel;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;

/**
 * Created by ananas on 29/04/18.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(DAOServiceFactory.class)
public class AdminAuthorizationControllerTest {

    @Before
    public void before()
    {
    }

    //@Mock
    //public DAOServiceFactory DAOServiceFactory;

    @Test
    public void authenticate() throws Exception {


//        PowerMock.suppressConstructor(DAOServiceFactory.class);

        PowerMock.mockStatic(DAOServiceFactory.class);

        DAOServiceFactory dao_mock = PowerMock.createMock(DAOServiceFactory.class);
        EasyMock.expect(DAOServiceFactory.getInstance()).andReturn(dao_mock).anyTimes();

        IStaffDAOService service_mock = PowerMock.createMock(IStaffDAOService.class);


        PowerMock.replay(DAOServiceFactory.class);
        EasyMock.expect(dao_mock.createService(IStaffDAOService.class)).andReturn(service_mock).anyTimes();

        PowerMock.replay(IStaffDAOService.class);
        //PowerMock.replay(dao_mock);


        Staff staff = new Staff();

        EasyMock.expect(service_mock.findStaff( EasyMock.anyString(), EasyMock.anyString())).andReturn(staff).anyTimes();


        HttpSession session_mock = PowerMock.createMock(HttpSession.class);

        Errors errors_mock = PowerMock.createMock(Errors.class);
        EasyMock.expect(errors_mock.hasErrors()).andReturn(Boolean.FALSE);


        MessageSource messageSource = PowerMock.createMock(MessageSource.class);

         AdminAuthorizationController tested_controller = new AdminAuthorizationController(messageSource);

        AuthorizationRequestModel model = new AuthorizationRequestModel();
        model.setPassword("123456");
        model.setMail("m@m.ru");
        tested_controller.authenticate(model, errors_mock, session_mock);


    }
    @Test
    public void logOut() throws Exception {

    }

}