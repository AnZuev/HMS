package ru.innopolis.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Создано: Денис
 * Дата:  09.11.2016
 * Описание: Контроллер страниц (jsp, html и т.д.)
 */
@Controller
public class StaticController {

    private static final String INDEX_PAGE_NAME = "index";

    @RequestMapping("/")
    public String getStartPage(){
        return INDEX_PAGE_NAME;
    }
}
