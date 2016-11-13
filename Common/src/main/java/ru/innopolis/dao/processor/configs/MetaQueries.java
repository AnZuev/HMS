package ru.innopolis.dao.processor.configs;


import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Создано: Денис
 * Дата:  13.11.2016
 * Описание: Объектное представление списка SQL запросов
 */
@XmlRootElement(name = "queries")
@XmlAccessorType(XmlAccessType.FIELD)
public class MetaQueries {

    @XmlElement(name = "queryDesc")
    private List<MetaQuery> queries;

    public List<MetaQuery> getQueries() {
        return queries;
    }

    public void setQueries(List<MetaQuery> queries) {
        this.queries = queries;
    }

    /**
     * Описание SQL запроса
     */
    @XmlRootElement(name = "queryDesc")
    @XmlAccessorType (XmlAccessType.FIELD)
    public static class MetaQuery {
        @XmlAttribute
        private String name;
        private String query;
        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }
}


