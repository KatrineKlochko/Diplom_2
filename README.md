# **Автоматизация тестов для приложения Stellar Burgers**

**Задание 2. Автотесты для API**

Этот проект содержит набор тестов эндпоинтов API для приложения бургерной Stellar Burgers

Тесты написаны с использованием JUnit 4, REST Assured и Allure 

Используемые технологии:

| технология          | версия |
|---------------------|--------|
| Java                | 11     |
| Maven               |        |
| REST Assured        | 5.5.6  |
| Allure Framework    | 2.15.0 |
| JUnit               | 4.13.2 |
| Gson                | 2.13.2 |
| AspectJ Weaver      | 1.9.21 |
| Allure Maven Plugin | 2.10.0 |



Запустить все тесты можно с помощью команды: mvn clean test

Сформировать и открыть отчёт после запуска тестов: allure serve target/allure-results
