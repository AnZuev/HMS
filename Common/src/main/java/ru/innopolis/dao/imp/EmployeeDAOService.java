package ru.innopolis.dao.imp;

import org.multylanguages.message.MetaMessage;
import ru.innopolis.dao.IEmployeeDAOService;
import ru.innopolis.dao.entity.Employee;
import ru.innopolis.dao.processor.ISQLProcessor;
import ru.innopolis.exceptions.UserErrorCode;
import ru.innopolis.exceptions.UserException;

import java.text.MessageFormat;
import java.util.List;

/**
 * Создано: Денис
 * Дата:  06.11.2016
 * Описание: Реализация сервиса по работе со сотрудниками гостиницы
 */
public class EmployeeDAOService implements IEmployeeDAOService {

    private static final MessageFormat checkEmailCondition = new MessageFormat("email=''{0}''");
    private static final String TYPE_OWNER = "TYPE='OWNER'";
    private ISQLProcessor sqlProcessor;

    public EmployeeDAOService(ISQLProcessor sqlProcessor) {
        this.sqlProcessor = sqlProcessor;
    }

    public void addOrUpdate(Employee employee) throws Exception {
        Long id = employee.getId();

        if (id == null) {
            checkEmail(employee);
            sqlProcessor.insert(employee);
        } else {
            Employee currentInf = sqlProcessor.getByID(Employee.class, id);
            if(!currentInf.getMail().equals(employee.getMail())){
                checkEmail(employee);
            }
            sqlProcessor.update(employee);
        }
    }

    private void checkEmail(Employee employee) throws Exception {
        String format = checkEmailCondition.format(new Object[]{employee.getMail()});
        List<Employee> employees = sqlProcessor.simpleSelect(Employee.class, format);
        if (!employees.isEmpty()) {
            MetaMessage message = new MetaMessage("employee.email.exists");
            throw new UserException(message, UserErrorCode.BAD_PARAMETERS);
        }
    }

    public List<Employee> getOwners() throws Exception {
        return sqlProcessor.simpleSelect(Employee.class, TYPE_OWNER);
    }
}
