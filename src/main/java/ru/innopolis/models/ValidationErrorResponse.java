package ru.innopolis.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Создано: Денис
 * Дата:  28.10.2016
 * Описание: Класс инкапсулирует в себе список ошибок валидации
 */
public class ValidationErrorResponse {

    private class ErrorMessage {
        private String field;
        private String message;

        private ErrorMessage(String field, String message) {
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

    private List<ErrorMessage> errors = new ArrayList<>();

    /**
     * Добавить запись об ошибке
     * @param fieldName Название поля
     * @param message Сообщение
     */
    public void addError(String fieldName, String message){
        ErrorMessage erMes = new ErrorMessage(fieldName, message);
        errors.add(erMes);
    }

    /**
     * Получить список ошибок
     * @return Список ошибок
     */
    public List<ErrorMessage> getErrors() {
        return errors;
    }
}
