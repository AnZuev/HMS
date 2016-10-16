package ru.innopolis.models;

/**
 * Создано: Денис
 * Дата:  14.10.2016
 * Описание:
 */

public class Greeting {

    private final long id;
    private final String content;

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
