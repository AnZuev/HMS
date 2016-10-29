package ru.innopolis.models;

import java.util.LinkedList;
import java.util.List;

/**
 * Создано: Денис
 * Дата:  28.10.2016
 * Описание: Класс инкапсулирует в себе список ошибок валидации
 */
public class ErrorResponse {

    private class FieldErrorMessage {
        private String field;
        private String message;

        private FieldErrorMessage(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }

    private List<FieldErrorMessage> fieldErrors = new LinkedList<>();
    private List<String> commonErrors = new LinkedList<>();
    /**
     * Добавить запись об ошибке
     * @param fieldName Название поля
     * @param message Сообщение
     */
    public void addFieldError(String fieldName, String message){
        FieldErrorMessage erMes = new FieldErrorMessage(fieldName, message);
        fieldErrors.add(erMes);
    }

    /**
     * Добавить ошибку для отображения пользователю
     * @param message Сообщение об ошибке
     */
    public void addCommonError(String message){
        commonErrors.add(message);
    }
    /**
     * Получить список ошибок валидации
     * @return Список ошибок валидации
     */
    public List<FieldErrorMessage> getFieldErrors() {
        return fieldErrors;
    }

    /**
     * Получить спиок ошибок
     * @return Список ошибок
     */
    public List<String> getCommonErrors() {
        return commonErrors;
    }
}
