package ru.innopolis.controllers;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import ru.innopolis.models.ValidationErrorResponse;

import java.util.List;
import java.util.Locale;

/**
 * Создано: Денис
 * Дата:  28.10.2016
 * Описание: Базовый класс для REST контролеров
 */
public abstract class BaseRestController {

    private MessageSource messageSource;

    public BaseRestController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Получить ответ, содержащий список ошибок валидации
     * @param bindingResult Результат валидации
     * @return Ответ, содержащий ошибки или null если ошибок нету.
     */
    protected ResponseEntity<ValidationErrorResponse> getValidationErrorResponse(Errors bindingResult){
        ResponseEntity<ValidationErrorResponse> responseEntity = null;
        if (bindingResult.hasErrors())
        {
            Locale locale = LocaleContextHolder.getLocale();
            List<FieldError> allErrors = bindingResult.getFieldErrors();
            ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse();
            for(FieldError error: allErrors){
                String message = messageSource.getMessage(error, locale);
                validationErrorResponse.addError(error.getField(), message);
            }
            responseEntity = new ResponseEntity<>(validationErrorResponse, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }
}
