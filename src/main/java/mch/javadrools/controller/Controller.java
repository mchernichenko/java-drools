package mch.javadrools.controller;

import mch.javadrools.model.Test;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${base-url}")
public class Controller {

    @Autowired
    KieContainer kieContainer;

    @GetMapping("/welcome")
    public String root() {
        return "Welcome to Spring";
    }

    @GetMapping("/getSla")
    public Test gatSla(@RequestParam String type) {
        Test test = new Test();
        test.setType(type);
        if (type.equalsIgnoreCase("ui")) {
            test.setSla("20");
        } else {
            test.setSla("50");
        }
        return test;
    }

    // Пример с использованием правил Drools
    @GetMapping("/getSlaWithDrool")
    public Test gatDroolSla(@RequestParam String type) {
        Test test = new Test();
        test.setType(type);

        KieSession kieSession = kieContainer.newKieSession("rulesSession");
        kieSession.insert(test);
        kieSession.fireAllRules();
        kieSession.dispose();

        return test;
    }

}