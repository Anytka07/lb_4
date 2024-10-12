package org.example;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Service;
import org.springframework.data.repository.Repository;

/*
  @author   Anna Melnychuk
  @project   lb_5
  @class  group 444A
  @version  1.0.0
  @since 12.10.24 - 13:10
*/

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@SpringBootTest
class MainArchitectureTest {

    private JavaClasses applicationClasses;

    @BeforeEach
    void initialize() {
        applicationClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_ARCHIVES)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("org.example");
    }

    // Перевірка, що контролери, сервіси та репозиторії дотримуються архітектурних правил шарів
    @Test
    void shouldFollowLayerArchitecture()  {
        layeredArchitecture()
                .consideringAllDependencies()
                .layer("Controller").definedBy("..controller..")
                .layer("Service").definedBy("..service..")
                .layer("Repository").definedBy("..repository..")
                //
                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller", "Service")
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
                //
                .check(applicationClasses);
    }

    // Контролери не повинні залежати від інших контролерів
    @Test
    void controllersShouldNotDependOnOtherControllers() {
        noClasses()
                .that().resideInAPackage("..controller..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..controller..")
                .because("Controllers should not depend on other controllers")
                .check(applicationClasses);
    }

    // Репозиторії не повинні залежати від сервісів
    @Test
    void repositoriesShouldNotDependOnServices() {
        noClasses()
                .that().resideInAPackage("..repository..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..service..")
                .because("Repositories should not depend on services")
                .check(applicationClasses);
    }

    // Класи контролерів повинні мати суфікс 'Controller'
    @Test
    void controllerClassesShouldBeNamedXController() {
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .haveSimpleNameEndingWith("Controller")
                .check(applicationClasses);
    }

    // Контролери повинні бути анотовані @RestController
    @Test
    void controllerClassesShouldBeAnnotatedWithRestController() {
        classes()
                .that().resideInAPackage("..controller..")
                .should()
                .beAnnotatedWith(RestController.class)
                .check(applicationClasses);
    }

    // Репозиторії повинні бути інтерфейсами
    @Test
    void repositoryShouldBeInterface() {
        classes()
                .that().resideInAPackage("..repository..")
                .should()
                .beInterfaces()
                .check(applicationClasses);
    }

    // Поля контролерів не повинні бути анотовані @Autowired
    @Test
    void controllerFieldsShouldNotBeAnnotatedWithAutowired() {
        noFields()
                .that().areDeclaredInClassesThat().resideInAPackage("..controller..")
                .should().beAnnotatedWith(Autowired.class)
                .check(applicationClasses);
    }


    // Контролери не повинні залежати від репозиторіїв
    @Test
    void controllersShouldNotDependOnRepositories() {
        noClasses()
                .that().resideInAPackage("..controller..")
                .should().dependOnClassesThat().resideInAPackage("..repository..")
                .check(applicationClasses);
    }
    

    // Поля моделей повинні бути приватними
    @Test
    void modelFieldsShouldBePrivate() {
        fields()
                .that().areDeclaredInClassesThat().resideInAPackage("..model..")
                .should().notBePublic()
                .check(applicationClasses);
    }

    // Сервісні класи повинні бути анотовані @Service
    @Test
    void serviceClassesShouldBeAnnotatedWithService() {
        classes()
                .that().resideInAPackage("..service..")
                .should().beAnnotatedWith(Service.class)
                .check(applicationClasses);
    }


    // Класи сервісів повинні мати суфікс 'Service'
    @Test
    void serviceClassesShouldHaveServiceSuffix() {
        classes()
                .that().resideInAPackage("..service..")
                .should().haveSimpleNameEndingWith("Service")
                .check(applicationClasses);
    }

    // Сервіси повинні мати анотацію @Service
    @Test
    void servicesShouldBeAnnotatedWithService() {
        classes()
                .that().resideInAPackage("..service..")
                .should().beAnnotatedWith(Service.class)
                .check(applicationClasses);
    }

    // Сервіси не повинні мати статичних методів
    @Test
    void servicesShouldNotHaveStaticMethods() {
        noMethods()
                .that().areDeclaredInClassesThat().resideInAPackage("..service..")
                .should().beStatic()
                .check(applicationClasses);
    }

    // Класи у пакеті model не повинні залежати від сервісів
    @Test
    void modelClassesShouldNotDependOnServices() {
        noClasses()
                .that().resideInAPackage("..model..")
                .should().dependOnClassesThat().resideInAPackage("..service..")
                .check(applicationClasses);
    }

    // Сервісні класи повинні бути фінальними
    /*@Test
    void serviceClassesShouldBeFinal() {
        classes()
                .that().resideInAPackage("..service..")
                .should().beFinal()
                .check(applicationClasses);
    }*/

    // Класи в пакеті service повинні бути публічними
    @Test
    void serviceClassesShouldBePublic() {
        classes()
                .that().resideInAPackage("..service..")
                .should().bePublic()
                .check(applicationClasses);
    }

    // Контролери не повинні мати статичних полів
    @Test
    void controllersShouldNotHaveStaticFields() {
        noFields()
                .that().areDeclaredInClassesThat().resideInAPackage("..controller..")
                .should().beStatic()
                .check(applicationClasses);
    }

    // Усі методи контролерів повинні бути публічними
    @Test
    void controllerMethodsShouldBePublic() {
        methods()
                .that().areDeclaredInClassesThat().resideInAPackage("..controller..")
                .should().bePublic()
                .check(applicationClasses);
    }

    // Методи у сервісах не повинні кидати загальні винятки (Exception)
    @Test
    void serviceMethodsShouldNotThrowGenericExceptions() {
        methods()
                .that().areDeclaredInClassesThat().resideInAPackage("..service..")
                .should().notDeclareThrowableOfType(Exception.class)
                .check(applicationClasses);
    }


    // Жоден клас у пакеті controller не повинен мати залежностей від інших контролерів
    @Test
    void controllerClassesShouldNotHaveDependenciesOnOtherControllers() {
        noClasses()
                .that().resideInAPackage("..controller..")
                .should().dependOnClassesThat().resideInAPackage("..controller..")
                .check(applicationClasses);
    }

    // Усі методи класів у пакеті model повинні бути non-static
    @Test
    void modelMethodsShouldNotBeStatic() {
        methods()
                .that().areDeclaredInClassesThat().resideInAPackage("..model..")
                .should().notBeStatic()
                .check(applicationClasses);
    }

    // Поля у класах model не повинні бути public
    @Test
    void modelFieldsShouldNotBePublic() {
        fields()
                .that().areDeclaredInClassesThat().resideInAPackage("..model..")
                .should().notBePublic()
                .check(applicationClasses);
    }

    // Класи моделей можуть мати публічні або захищені конструктори
    @Test
    void modelClassesShouldHavePublicOrProtectedConstructors() {
        constructors()
                .that().areDeclaredInClassesThat().resideInAPackage("..model..")
                .should().bePublic()
                .orShould().beProtected()
                .check(applicationClasses);
    }
}
