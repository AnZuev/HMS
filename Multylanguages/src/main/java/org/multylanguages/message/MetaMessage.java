package org.multylanguages.message;

/**
 * Создано: Денис
 * Дата:  06.09.2016
 * Описание: Класс описывающий сообщения, отображаемые пользователю
 */
public class MetaMessage {
    private String key;
    private Object[] parameters;

    public MetaMessage(String key, Object[] parameters) {
        this.key = key;
        this.parameters = parameters;
    }

    public MetaMessage(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public Object[] getParameters() {
        return parameters;
    }
}
