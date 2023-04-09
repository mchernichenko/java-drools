Drools является частью сообщества KIE (Knowledge Is Everything) с открытым исходным кодом, которое состоит из различных связанных проектов или групп проектов,
которые дополняют портфель решений для автоматизации и управления бизнесом.
Один из них jBPM - это набор инструментов для создания бизнес-приложений, помогающих автоматизировать бизнес-процессы и решения.

KIE :: Public API  https://docs.drools.org/latest/kie-api-javadoc/index.html

---

**KieServices** - потокобезопасный синглтон, действующий как точка входа, предоставляющий доступ к другим сервисам, предоставляемым Kie, т.е.
через который получаются различные объекты для завершения построения, управления и выполнения правил.

**KieFileSystem** своего рода виртуальная in-memory файловая система

**KieContainer** - контейнер для всех KieBases конкретного KieModule

	!!!NB: на этапе создания контейнера, точнее, на этапе билда KieFileSystem, создаются KieModule, которые в свою очеред наполняются KieBases.
    На этапе создания KieBases идёт обращение к kmodule.xml и обнаружение всех KieModules в classpath, через файл kmodule.xml
	Каждый полученный KieModule добавляется в KieRepository
	KieContainer и KieModule немного сложнее отличить, но на практике они в основном одно и то же (контейнер - это реализация во время выполнения, модуль - спецификация).
	Отличие: Из контейнера можно получить KieSession,а из KieModule нет	
	KieContainer предоставляет среду выполнения для использования KieModule
	KieContainer создаётся с помощью модуля
		KieContainer kContainer = kieServices.newKieContainer(kieModule.getReleaseId());
	Когда нашему приложению требуется запустить определенный тип правил, мы извлекаем нужный KieBase из KieServices (через KieContainer), запрашиваем сеанс и запускаем правила

	KieRepository - это синглтон, представляет хранилище для всех доступных KieModules, независимо от того, хранятся ли они в репозитории maven или собраны пользователем программно.

	KieBase - это набор правил. Также содержит процессы, функции и модели типов. 
	Сама KieBase не содержит данных; вместо этого из KieBase создаются сессии, в которые могут быть вставлены данные и из которых могут быть запущены экземпляры процессов.
	KieBase можно получить из KieContainer, содержащего KieModule, в котором определена KieBase.	
	
	Может быть создано несколько KieBase`ом, т.е. набор вязанных правил можно логически выделить в отдельный KieBase, например, 
	правила для мониторинга событий  можно отделить, например от правил проверки для входящих запросов

	KieModule - это контейнер KieBases. Владеет необходимой информацией для создания набора KieBases, таких как:
		- pom.xml
		- определяющий его ReleaseId
			ReleaseId - это полный идентификатор версии артефакта. В соответствии с конвенциями Maven он состоит из трех частей: groupId, artifactId и version
		- файл kmodule.xml, объявляющий имена и конфигурации
	Модули могут содержать несколько баз знаний (Knowledge Bases), в которых могут проводиться различные сессии.	
	
	   
	kmodule.xml -  декларативно определят KieBases и KieSessions, которые могут быть созданы на его основе.		
	Этот файл должен быть помещен в папку resources/META-INF проекта Maven, а все остальные артефакты Kie, такие как файлы DRL или Excel, должны храниться в папке resources или в любой другой подпапке под ней   
	самый простой файл kmodule.xml может содержать просто пустой тег kmodule, как показано ниже:
		<?xml version="1.0" encoding="UTF-8"?>
		<kmodule xmlns="http://www.drools.org/xsd/kmodule"/>
	Таким образом, kmodule будет содержать один единственный KieBase по умолчанию. Все Kie-ресурсы, хранящиеся в папке resources или любой из ее подпапок, будут скомпилированы и добавлены в KieBase.
	Чтобы вызвать сборку этих артефактов, достаточно создать для них KieContainer.


**KieSession** получается из контейнера.
Сессия - содержит все ресурсы для исполнения правил. Все факты вставляются в сессиию, затем запускаются правила.
Аналог сессии с БД, где БД хранилище данных, но чтобы управлять данными, нужно установить сессию.

---
    // load up the knowledge base
    KieServices ks = KieServices.Factory.get();
    KieContainer kContainer = ks.getKieClasspathContainer();  // получение контейнера, можно указать classLoader, здесь по умолчанию
    // контейнер можно и создать ks.newKieContainer(kieModule.getReleaseId()); 			
    KieSession kSession = kContainer.newKieSession("ksession-rules");
    try {
    kieSession.insert(new Fibonacci(10));
    kieSession.fireAllRules();
    } finally {
    kieSession.dispose();
    }
----
    KieServices kieServices = KieServices.Factory.get();
    
    KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
    kieFileSystem.write(ResourceFactory.newClassPathResource(drlFile));
    KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
    kieBuilder.buildAll();
    KieModule kieModule = kieBuilder.getKieModule();
    
    return kieServices.newKieContainer(kieModule.getReleaseId());

----

KieSession ksession = new DroolsBeanFactory().getKieSession();

1. Создание KieServices 

        KieServices kieServices = KieServices.Factory.get();

2. Cоздение репозитория и добавление в него дефалтового модуля. 

       KieRepository kieRepository = kieServices.getRepository();
       kieRepository.addKieModule(kieRepository::getDefaultReleaseId);

3. Создание KieFileSystem и наполнение её правилами.

       KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
       kieFileSystem.write(ResourceFactory.newClassPathResource("com/baeldung/drools/rules/BackwardChaining.drl"));
       kieFileSystem.write(ResourceFactory.newClassPathResource("com/baeldung/drools/rules/SuggestApplicant.drl"));
       kieFileSystem.write(ResourceFactory.newClassPathResource("com/baeldung/drools/rules/Product_rules.drl.xls"));

4. Билдим (монтируем) виртуальную файловую систему. Т.е. создание объектов KieModule

       KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);
       kb.buildAll(); // собирает все ресурсы и привязывает их к KieBase. Он успешно выполняется только тогда, когда ему удается найти и проверить все файлы правил.

5. Создём контенер с указанием модуля и получаем сессию

       KieModule kieModule = kb.getKieModule();
       KieContainer kContainer = kieServices.newKieContainer(kieModule.getReleaseId());
       kContainer.newKieSession();

---
    ksession.setGlobal("result", result);
    ksession.insert(new Fact("Asia", "Planet Earth"));
    ksession.insert(new Fact("China", "Asia"));
    ksession.insert(new Fact("Great Wall of China", "China"));
    
    ksession.fireAllRules();