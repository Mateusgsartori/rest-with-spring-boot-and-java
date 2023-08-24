package spring.project.restwithspringbootandjava;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingsController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greetings")
    public Greetintgs greetintgs(@RequestParam(value = "name", defaultValue = "world") String name){
        return new Greetintgs(counter.incrementAndGet(), String.format(template, name));
    }

}
