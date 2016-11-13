package ru.innopolis.dao.imp;

import org.apache.commons.lang3.StringUtils;
import org.multylanguages.message.MetaMessage;
import ru.innopolis.dao.IEmployeeDAOService;
import ru.innopolis.dao.entity.Employee;
import ru.innopolis.dao.entity.addition.ExtendedEmployee;
import ru.innopolis.dao.processor.ISQLProcessor;
import ru.innopolis.exceptions.UserErrorCode;
import ru.innopolis.exceptions.UserException;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

/**
 * Создано: Денис
 * Дата:  06.11.2016
 * Описание: Реализация сервиса по работе со сотрудниками гостиницы
 */
public class EmployeeDAOService implements IEmployeeDAOService {

    private static final MetaMessage EMPLOYEE_DOES_NOT_EXIST_MESSAGE = new MetaMessage("employee.does.not.exist");
    private static final MetaMessage EMPLOYEE_EMAIL_EXISTS_MESSAGE = new MetaMessage("employee.email.exists");
    private static final MetaMessage PASSWORD_CAN_NOT_BE_EMPTY_MESSAGE = new MetaMessage("password.can.not.be.empty");
    private static final String GET_LIST_OF_OWNERS_WITH_HOTEL_DESC = "GetListOfOwnersWithHotelDesc";

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
            if (StringUtils.isEmpty(employee.getPassword())){
                throw new UserException(PASSWORD_CAN_NOT_BE_EMPTY_MESSAGE, UserErrorCode.BAD_PARAMETERS);
            }
            checkEmail(employee);
            sqlProcessor.insert(employee);
        } else {
            Employee currentInf = sqlProcessor.getByID(Employee.class, id);

            if (currentInf == null){
                throw new UserException(EMPLOYEE_DOES_NOT_EXIST_MESSAGE, UserErrorCode.BAD_PARAMETERS);
            }

            if(!currentInf.getMail().equals(employee.getMail())){
                checkEmail(employee);
            }
            if (StringUtils.isEmpty(employee.getPassword())){
                employee.setPassword(currentInf.getPassword());
            }
            sqlProcessor.update(employee);
        }
    }

    private void checkEmail(Employee employee) throws Exception {
        String format = checkEmailCondition.format(new Object[]{employee.getMail()});
        List<Employee> employees = sqlProcessor.simpleSelect(Employee.class, format);
        if (!employees.isEmpty()) {
            throw new UserException(EMPLOYEE_EMAIL_EXISTS_MESSAGE, UserErrorCode.BAD_PARAMETERS);
        }
    }

    public List<ExtendedEmployee> getOwners() throws Exception {
        return sqlProcessor.executeSelect(ExtendedEmployee.class, GET_LIST_OF_OWNERS_WITH_HOTEL_DESC, Collections.emptyMap());
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
            throw new UserException(EMPLOYEE_DOES_NOT_EXIST_MESSAGE, UserErrorCode.BAD_PARAMETERS);
        }
    }
}
