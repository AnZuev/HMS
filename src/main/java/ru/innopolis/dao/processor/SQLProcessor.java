package ru.innopolis.dao.processor;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Создано: Денис
 * Дата:  02.11.2016
 * Описание: Процессор для выолнения запросов в БД
 */
public class SQLProcessor implements ISQLProcessor {

    private static final String THERE_IS_NOT_TABLE_NAME = "Не задо название таблицы для ";
    private static final String THERE_IS_NOT_PK = "Не задан первичный ключ";
    private static final String WHERE_CAUSE = "WHERE ";
    private static final String NULL_VALUE = "null";
    private static final String COMMA = ",";
    private static final String MARKS = "'";
    private static final String EQUALS_SIGN = "=";
    private static final MessageFormat DELETE_ST = new MessageFormat("DELETE FROM {0} where {1}= {2}");
    private static final MessageFormat INSERT_ST = new MessageFormat("INSERT INTO {0} ({1}) VALUES({2})");
    private static final MessageFormat UPDATE_ST = new MessageFormat("UPDATE {0} SET {1} WHERE {2} = {3}");
    private static final MessageFormat SELECT_ST = new MessageFormat("SELECT {0} FROM {1} {2}");
    private static final MessageFormat NEXT_VALUE_SEQ_ST = new MessageFormat("SELECT {0}.NEXTVAL ID FROM DUAL");
    private static final MessageFormat TO_DATE = new MessageFormat("TO_DATE(''{0}-{1}-{2}'', ''YYYY-MM-DD'')");

    private DataSource source;

    /**
     * Базовый конструктор
     *
     * @param source Источник данных
     */
    public SQLProcessor(DataSource source) {
        this.source = source;
    }

    /**
     * Выполнить SQL запрос
     *
     * @param action Действие, которое необходимо выполнить
     * @param <T>    Возвращаемый тип
     * @return Результат операции
     */
    public <T> T execute(SQLAction<T> action) throws Exception {
        try (Connection connection = source.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                T result = action.execute(statement);
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    public <T> int delete(T object) throws Exception {
        checkNull(object);
        Field primaryKeyField = getPrimaryKeyField(object.getClass());
        Object property = PropertyUtils.getProperty(object, primaryKeyField.getName());
        if (property == null) {
            throw new Exception("Не задо значение первичного ключа");
        }

        Column column = primaryKeyField.getAnnotation(Column.class);
        String tableName = getTableName(object.getClass());
        String columnName = column.name();
        String deleteSt = DELETE_ST.format(new Object[]{tableName, columnName, property});
        int effectedRow = execute(s -> s.executeUpdate(deleteSt));
        return effectedRow;
    }

    public <T> int insert(T object) throws Exception {
        checkNull(object);
        Field pKField = getPrimaryKeyField(object.getClass());
        SequenceGenerator sq = pKField.getAnnotation(SequenceGenerator.class);
        long newID = generatePK(sq);

        PropertyUtils.setProperty(object, pKField.getName(), newID);
        String tableName = getTableName(object.getClass());
        String sequenceNames = getListColumnNames(object.getClass());
        String valueSeq = getValues(object);

        String insertSt = INSERT_ST.format(new Object[]{tableName, sequenceNames, valueSeq});

        try (Connection connection = source.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                int result = statement.executeUpdate(insertSt);
                connection.commit();
                return result;
            } catch (SQLException e) {
                connection.rollback();
                PropertyUtils.setProperty(object, pKField.getName(), null);
                throw e;
            }
        }
    }

    public <T> int update(T object) throws Exception {
        checkNull(object);
        Field primaryKeyField = getPrimaryKeyField(object.getClass());
        Object idValue = PropertyUtils.getProperty(object, primaryKeyField.getName());
        if (idValue == null) {
            throw new Exception("Не задо значение первичного ключа");
        }
        String tableName = getTableName(object.getClass());
        String setValuePairs = generateSetValuePairs(object);
        Column idColumn = primaryKeyField.getAnnotation(Column.class);
        String idName = idColumn.name();

        String updateSt = UPDATE_ST.format(new Object[]{tableName, setValuePairs, idName, idValue});
        int effectedRow = execute(s -> s.executeUpdate(updateSt));
        return effectedRow;
    }

    public <T> List<T> simpleSelect(Class<T> clazz, String whereClause) throws Exception {
        checkNull(clazz);
        String tableName = getTableName(clazz);
        String sequenceNames = getListColumnNames(clazz);
        if (StringUtils.isEmpty(whereClause)) {
            whereClause = StringUtils.EMPTY;
        } else {
            whereClause = WHERE_CAUSE + whereClause;
        }
        String selectSt = SELECT_ST.format(new Object[]{sequenceNames, tableName, whereClause});

        List<T> output = execute(s -> {
            s.execute(selectSt);
            try (ResultSet resultSet = s.getResultSet()) {
                return deserializeResponse(resultSet, clazz);
            } catch (Exception e) {
                throw new SQLException(e);
            }
        });
        return output;

    }

    public <T> List<T> executeSelect(Class<T> clazz, String queryPattern, Object[] arg) throws Exception {
        String[] sqlArg = new String[arg.length];
        for (int i = 0; i < arg.length; i++) {
            sqlArg[i] = convertToSQLValue(arg[i]);
        }
        MessageFormat format = new MessageFormat(queryPattern);
        String query = format.format(sqlArg);

        List<T> output = execute(s -> {
            s.execute(query);
            try (ResultSet resultSet = s.getResultSet()) {
                return deserializeResponse(resultSet, clazz);
            } catch (Exception e) {
                throw new SQLException(e);
            }
        });
        return output;
    }

    public <T> List<T> deserializeResponse(ResultSet resultSet, Class<T> clazz) throws Exception {
        List<T> list = new LinkedList<>();
        Field[] fields = clazz.getDeclaredFields();
        while (resultSet.next()) {
            T instance = clazz.newInstance();
            for (Field field : fields) {
                Column columnDesc = field.getAnnotation(Column.class);
                if (columnDesc != null) {
                    Class<?> type = field.getType();
                    Object value = resultSet.getObject(columnDesc.name(), type);
                    PropertyUtils.setProperty(instance, field.getName(), value);
                }
            }
            list.add(instance);
        }
        return list;
    }

    public <T> T getByID(Class<T> clazz, Long id) throws Exception {
        Field primaryKeyField = getPrimaryKeyField(clazz);
        Column pk = primaryKeyField.getAnnotation(Column.class);
        String condition = pk.name() + EQUALS_SIGN + id;
        List<T> list = simpleSelect(clazz, condition);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Проверка, является значение на null
     *
     * @param obj Значение
     * @Exception IllegalArgumentException Если значение = null
     */
    private void checkNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Получить имя таблицы, где хранятся экземпляры класса
     *
     * @param clazz Мета класс сущности, хранимой в БД
     * @param <T>   Тип сущности, хранимой в БД
     * @return Название таблицы
     * @throws Exception Не задо название таблицы
     */
    private <T> String getTableName(Class<T> clazz) throws Exception {
        Table annotation = clazz.getAnnotation(Table.class);
        if (annotation == null) {
            throw new Exception(THERE_IS_NOT_TABLE_NAME + clazz);
        }
        return annotation.name();
    }

    /**
     * Получить поле, хранящее первичный ключ
     *
     * @param clazz Мета описание класса
     * @param <T>    Тип сущности
     * @return Поле, хранящее первичный ключ
     * @throws Exception Не задан первичный ключ
     */
    private <T> Field getPrimaryKeyField(Class<T> clazz) throws Exception {
        Field[] declaredFields = clazz.getDeclaredFields();
        Field primaryKeyField = null;
        for (Field field : declaredFields) {
            Id annotation = field.getAnnotation(Id.class);
            if (annotation != null) {
                primaryKeyField = field;
                break;
            }
        }
        if (primaryKeyField == null) {
            throw new Exception(THERE_IS_NOT_PK);
        }
        return primaryKeyField;
    }

    /**
     * Сгенерировать первичный ключ
     *
     * @param generator Генератор ключа
     * @return Ключ
     */
    private long generatePK(SequenceGenerator generator) throws Exception {
        String name = generator.name();
        String query = NEXT_VALUE_SEQ_ST.format(new Object[]{name});
        long id = execute(s -> {
            s.execute(query);
            try (ResultSet resultSet = s.getResultSet()) {
                resultSet.next();
                return resultSet.getLong(1);
            }
        });
        return id;
    }

    /**
     * Получить список колонок
     *
     * @param clazz Описание сущности, хранимое в БД
     * @return Список колонок через запятую
     */
    private String getListColumnNames(Class clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        StrBuilder builder = new StrBuilder(100);
        for (int i = declaredFields.length - 1; i >= 0; i--) {
            Field field = declaredFields[i];
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                builder.append(column.name());
                if (i != 0) {
                    builder.append(COMMA);
                }
            }
        }
        return builder.toString();
    }

    /**
     * Получить список значений, хранящихся в экземпляре класса
     *
     * @param object Экземпляр класса
     * @param <T>    Тип сущности
     * @return Список значений через запятую
     */
    private <T> String getValues(T object) throws Exception {
        Field[] declaredFields = object.getClass().getDeclaredFields();
        StrBuilder builder = new StrBuilder(100);
        for (int i = declaredFields.length - 1; i >= 0; i--) {
            Field field = declaredFields[i];
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                Object value = PropertyUtils.getProperty(object, field.getName());
                String result = convertToSQLValue(value);
                builder.append(result);
                if (i != 0) {
                    builder.append(COMMA);
                }
            }
        }
        return builder.toString();
    }

    /**
     * Сгенерировать пары ключ=значения
     *
     * @param object Экземпляр класса
     * @param <T>    Тип сущности
     * @return Пары ключ=значения через запятую
     */
    private <T> String generateSetValuePairs(T object) throws Exception {
        StringBuilder builder = new StringBuilder(100);
        Field[] fields = object.getClass().getDeclaredFields();
        List<String> keyValue = new ArrayList<>(fields.length);
        for (int i = fields.length - 1; i >= 0; i--) {
            Field field = fields[i];
            Id pk = field.getAnnotation(Id.class);
            if (pk == null) {
                Column c = field.getAnnotation(Column.class);
                Object value = PropertyUtils.getProperty(object, field.getName());
                String sqlValue = convertToSQLValue(value);
                keyValue.add(c.name() + EQUALS_SIGN + sqlValue);
            }
        }
        for (int i = keyValue.size() - 1; i >= 0; i--) {
            String pair = keyValue.get(i);
            builder.append(pair);
            if (i != 0) {
                builder.append(COMMA);
            }
        }
        return builder.toString();
    }

    /**
     * Конвертировать значение java объекта в строковое представление, принимаемое БД
     *
     * @param value Значение для конвертации
     * @return Сконвертированное значение
     */
    private String convertToSQLValue(Object value) {
        String result;
        if (value instanceof String) {
            result = MARKS + value + MARKS;
        } else if (value instanceof Calendar) {
            Calendar c = (Calendar) value;
            Integer year = c.get(Calendar.YEAR);
            Integer month = c.get(Calendar.MONTH);
            Integer day = c.get(Calendar.DAY_OF_MONTH);
            Object[] args = new Object[]{year.toString(), month.toString(), day.toString()};
            result = TO_DATE.format(args);
        } else if (value != null) {
            result = value.toString();
        } else {
            result = NULL_VALUE;
        }
        return result;
    }


}
