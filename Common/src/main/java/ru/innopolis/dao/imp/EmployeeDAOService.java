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

    private static final MetaMessage EMPLOYEE_DOES_NOT_EXIST_MESSAGE = new MetaMessage("employee.does.not.exist");

    private MessageFormat findEmployeeCondition = new MessageFormat("email = ''{0}'' and HASH_PASSWORD = ''{1}''");
    private MessageFormat findManagersCondition = new MessageFormat("HOTEL_ID={0} and TYPE=''MANAGER''");
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

    public Employee findEmployee(String email, String hashPassword) throws Exception {
        String where = findEmployeeCondition.format(new Object[]{email, hashPassword});
        List<Employee> staffs = sqlProcessor.simpleSelect(Employee.class, where);
        return staffs.isEmpty() ? null : staffs.get(0);
    }

    public List<Employee> getManagersByHotelId(long hotelId) throws Exception {
        String condition = findManagersCondition.format(new Object[]{Long.toString(hotelId)});
        List<Employee> employees = sqlProcessor.simpleSelect(Employee.class, condition);
        return employees;
    }

    public void delete(Employee employee) throws Exception {
        int countOfEffectedRecords = sqlProcessor.delete(employee);
        if (countOfEffectedRecords == 0){
            throw new UserException(EMPLOYEE_DOES_NOT_EXIST_MESSAGE);
        }
    }
}
