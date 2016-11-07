package org.multylanguages.exeption;

import org.multylanguages.message.MetaMessage;

/**
 * Создано: Денис
 * Дата:  19.09.2016
 * Описание:
 */
public class MetaMessageException extends Exception {
    private MetaMessage metaMessage;

    public MetaMessageException(MetaMessage metaMessage) {
        this.metaMessage = metaMessage;
    }

    public MetaMessage getMetaMessage() {
        return metaMessage;
    }
}
