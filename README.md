Test Task




Веб-приложение реализует добавление, редактирование и удаление следующих сущностей: банк, клиент, кредит, кредитное предложение, процесс оформления кредита на клиента с созданием графика платежей и расчетом
необходимых сумм, автоматический расчет итоговой суммы процентов по кредиту, автоматический расчет суммы ежемесячного платежа с учетом процентной ставки.


Примечание:<br>
- Приложение стало основой для дипломного проекта и теперь к нему постепенно доавляются всякие фичи;
- оформление кредита - вкладка "Кредитное предложение" -> выбираем из списка банк с нужным кредитом, выбираем клиента, "Оформить кредит"<br>
оформленные кредиты можно посмотреть во вкладке "Банк" -> Информация о банке<br>
удалить оформленый кредит можно во вкладке "Банк" -> Редактировать банк




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

