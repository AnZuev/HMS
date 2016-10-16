package ru.innopolis.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.innopolis.models.Greeting;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Создано: Денис
 * Дата:  14.10.2016
 * Описание:
 */
@RestController
public class ExampleController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/world/{name}")
    public Greeting greeting(@PathVariable String name, Model model){
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

}
