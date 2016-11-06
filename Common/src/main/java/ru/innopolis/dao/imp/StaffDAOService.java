package ru.innopolis.dao.imp;

import ru.innopolis.dao.IStaffDAOService;
import ru.innopolis.dao.entity.Staff;
import ru.innopolis.dao.processor.ISQLProcessor;

import java.text.MessageFormat;
import java.util.List;

/**
 * Создано: Денис
 * Дата:  06.11.2016
 * Описание: Реализация сервиса по работе с нашими сотрудниками
 */
public class StaffDAOService implements IStaffDAOService {

    private MessageFormat findClientCondition = new MessageFormat("email = ''{0}'' and HASH_PASSWORD = ''{1}''");

    private ISQLProcessor sqlProcessor;

    public StaffDAOService(ISQLProcessor sqlProcessor) {
        this.sqlProcessor = sqlProcessor;
    }

    public Staff findStaff(String email, String hashPassword) throws Exception {
        String where = findClientCondition.format(new Object[]{email, hashPassword});
        List<Staff> staffs = sqlProcessor.simpleSelect(Staff.class, where);
        return staffs.isEmpty() ? null : staffs.get(0);
    }
}
