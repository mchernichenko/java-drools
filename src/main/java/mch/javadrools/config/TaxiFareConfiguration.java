package mch.javadrools.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("mch.javadrools.service")
/**
 * Конфигурация Drools
 */
public class TaxiFareConfiguration {
    public static final String TaxiFareRule = "TaxiFareRule.drl";
    public static final String TestRule = "TestRule.drl";

    @Bean
    public KieContainer kieContainer() {
        // KieServices - это синглтон, который действует как единая точка входа для получения всех услуг, предоставляемых Kie.
        KieServices kieServices = KieServices.Factory.get();

        // Далее нам нужно получить KieContainer, который является держателем для всех объектов, необходимых для работы механизма правил.
        // KieContainer создается с помощью других бинов, включая KieFileSystem, KieBuilder и KieModule.
        // Подробно описано в README.md

        //Создание KieFileSystem и наполнение её правилами.
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource(TaxiFareRule));
       // kieFileSystem.write(ResourceFactory.newClassPathResource(TestRule));

        // Билдим (монтируем) виртуальную файловую систему. Т.е. создание объектов KieModule
        // buildAll() собирает все ресурсы и привязывает их к KieBase.
        // Он успешно выполняется только тогда, когда ему удается найти и проверить все файлы правил.
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieModule kieModule = kieBuilder.getKieModule();

        // После создания KieModule мы можем перейти к созданию KieContainer - который содержит KieModule, где определена KieBase.
        // KieContainer создается с помощью модуля:
        return kieServices.newKieContainer(kieModule.getReleaseId());

    }

   /* @Bean
    public KieContainer kieContainer() {
        // при создании KieServices автоматически создаётся дефалтовый контейнер, который берёт ресурсы из указанного в kmodule.xml пакета
        // такой вариант выглядит намного лаконичнее чем приведённый выше, но не показывает что происходит под капотом.
        return KieServices.Factory.get().getKieClasspathContainer();
    }*/
}

