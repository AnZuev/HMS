package ru.innopolis.controllers;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import ru.innopolis.models.ErrorResponse;

import java.util.List;
import java.util.Locale;

/**
 * Создано: Денис
 * Дата:  28.10.2016
 * Описание: Базовый класс для REST контролеров
 */
public abstract class BaseRestController {

    public static final String ERROR_COMMON = "error.common";
    private MessageSource messageSource;

    public BaseRestController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Получить ответ, содержащий список ошибок валидации
     * @param bindingResult Результат валидации
     * @return Ответ, содержащий ошибки или null если ошибок нету.
     */
    protected ResponseEntity<ErrorResponse> getValidationErrorResponse(Errors bindingResult){
        ResponseEntity<ErrorResponse> responseEntity = null;
        if (bindingResult.hasErrors())
        {
            Locale locale = LocaleContextHolder.getLocale();
            List<FieldError> allErrors = bindingResult.getFieldErrors();
            ErrorResponse errorResponse = new ErrorResponse();
            for(FieldError error: allErrors){
                String message = messageSource.getMessage(error, locale);
                errorResponse.addFieldError(error.getField(), message);
            }
            responseEntity = new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    protected ResponseEntity<ErrorResponse> getGeneralErrorResponse(){
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(ERROR_COMMON, null, locale);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.addCommonError(message);
        ResponseEntity<ErrorResponse> responseEntity = new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        return responseEntity;
    }
}
