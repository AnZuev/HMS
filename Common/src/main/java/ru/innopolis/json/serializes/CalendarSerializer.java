package ru.innopolis.json.serializes;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Calendar;

/**
 * Создано: Денис
 * Дата:  04.11.2016
 * Описание: К сожалению, стандартный сериализатор оперирует {@link java.util.TimeZone}, по этому результат
 * отображается не корректно. Данная реализация возвращает дату в формате ГГГГ-ММ-ДД
 */
public class CalendarSerializer extends StdSerializer<Calendar> {
    public CalendarSerializer(Class<Calendar> t) {
        super(t);
    }

    public CalendarSerializer() {
        super(Calendar.class);
    }

    public void serialize(Calendar value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        int year = value.get(Calendar.YEAR);
        //В Java месяцы нумеруются с 0
        int month = value.get(Calendar.MONTH) + 1;
        int day = value.get(Calendar.DAY_OF_MONTH);
        gen.writeString(year + "-" + addLeadingZero(month) + "-" + addLeadingZero(day));
    }

    private String addLeadingZero(int d){
        if (d < 10){
            return "0" + d;
        }
        return Integer.toString(d);
    }
}
