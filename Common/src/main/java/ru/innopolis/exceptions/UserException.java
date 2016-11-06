package ru.innopolis.exceptions;

import org.multylanguages.exeption.MetaMessageException;
import org.multylanguages.message.MetaMessage;

/**
 * Создано: Денис
 * Дата:  03.11.2016
 * Описание: Сообщение описывающее исключительную ситуацию, возникшую по вине пользователя
 */
public class UserException extends MetaMessageException {

    private Integer errorCode;

    /**
     * Базовый конструктор
     * @param metaMessage Мета сообщение об ошибке
     */
    public UserException(MetaMessage metaMessage) {
        super(metaMessage);
    }

    /**
     * Конструктор для сообщения и кода об ошибке
     * @param metaMessage Мета сообщение об ошибке
     * @param errorCode Код ошибки
     */
    public UserException(MetaMessage metaMessage, Integer errorCode) {
        super(metaMessage);
        this.errorCode = errorCode;
    }

    /**
     * Получить код ошибки
     * @return Код ошибки, если есть. В противном случае null.
     */
    public Integer getErrorCode() {
        return errorCode;
    }
}
