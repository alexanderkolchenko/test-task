Test Task

Веб-приложение реализует добавление, редактирование и удаление следующих сущностей: банк, клиент, кредит, кредитное предложение, процесс оформления кредита на клиента с созданием графика платежей и расчетом
необходимых сумм, автоматический расчет итоговой суммы процентов по кредиту, автоматический расчет суммы ежемесячного платежа с учетом процентной ставки.


https://github.com/alexanderkolchenko/test-task/tree/main/src/main/java/com/haulmont/testtask

файлы БД:
https://github.com/alexanderkolchenko/test-task/tree/main/src/main/resources/db

скрипт БД:
https://github.com/alexanderkolchenko/test-task/blob/main/src/main/resources/db/create_db.sql

JDK 8, Maven 3, Spring-Boot, Jetty Server

Build and Run in the command line:<br>
mvn package<br>
mvn spring-boot:run

Open http://localhost:8080 in a web browser.

