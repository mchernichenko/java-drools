package mch.javadrools.service;

import mch.javadrools.model.Fare;
import mch.javadrools.model.TaxiRide;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
// сервис расчёта
public class TaxiFareCalculatorService {
    @Autowired
    private KieContainer kContainer;

    public Long calculateFare(TaxiRide taxiRide, Fare rideFare) {

        // KieSession создается с помощью экземпляра KieContainer.
        // KieSession - это место, куда могут быть вставлены входные данные.
        // KieSession взаимодействует с движком для обработки фактической бизнес-логики, определенной в правиле на основе вставленных фактов.
        KieSession kieSession = kContainer.newKieSession();

        // Global (как и глобальная переменная) используется для передачи информации в движок.
        // в данном примере мы установили объект Fare в качестве Global для хранения рассчитанной стоимости проезда на такси.
        kieSession.setGlobal("rideFare", rideFare);
        kieSession.insert(taxiRide);  // закидываем факт в сессию
        kieSession.fireAllRules();    // прогоняем все правила, что зареганы в KieBase
        kieSession.dispose();         // удаляем сессию

        return rideFare.getTotalFare();
    }
}
