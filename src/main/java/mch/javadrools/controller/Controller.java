package mch.javadrools.controller;

import mch.javadrools.model.Fare;
import mch.javadrools.model.TaxiRide;
import mch.javadrools.model.Test;
import mch.javadrools.service.TaxiFareCalculatorService;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base-url}")
public class Controller {

    @Autowired
    KieContainer kieContainer;
    @Autowired
    TaxiFareCalculatorService taxiFareCalculatorService;

    @GetMapping("/welcome")
    public String root() {
        return "Welcome to Spring";
    }

    @PostMapping("/calcTaxiFare")
    public String calcTaxiFare(@RequestBody TaxiRide taxiRide) {
        Fare rideFare = new Fare();
        Long totalFire = taxiFareCalculatorService.calculateFare(taxiRide, rideFare);
        return  "!! RIDE FARE !! " + totalFire.toString();
    }

    @GetMapping("/getSla")
    public Test getSla(@RequestParam String type) {
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