## Test - Service Desk
### Task description:
Create simple Service Desk application. In application you can open new tickets. Creating
new tickets must happen in separate modal window.

Ticket should contain folowing fields:

* Title
* Automatically generated ticket number
* E-mail aadress
* Problem description
* Five level priority system

After confirming ticket, it is shown in list of tickets. Tickets should be able to change after
creation. After closing ticket, it should be removed from list. Tickets list should be sortable by
status, priotiry and date.

Bear in mind that this exercise must use best practices in development world. This application
must be covered with tests.

### Requirements:
* Java 1.8(+) (tested on Java 11)
* Open `8080` port

### Installation & Running

* Clone this repository
* From projects root directory:
    * If Maven installed globally: run `mvn spring-boot:run` 
    * Or use maven wrapper: run `./mvnw spring-boot:run` 
    * Or from IDE (Intellij IDEA should pick up configuration automatically)

#### Tests:
If using maven: `mvn test`

#### NB! What could be different, but for simplicity not implemented:
* Used migration for DB, such as FlyWay
* Normal RDBMS( Like Postgres/MySQL )
* Added some caching mechanism
* Swagger UI for testing API(for 4 routes not needed really..)

#### Contacts
Developer: Nikita Porshnjakov

[n.porshnjakov@gmail.com](mailto:n.porshnjakov@gmail.com)