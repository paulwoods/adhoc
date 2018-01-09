package org.mrpaulwoods.adhoc.core

import org.mrpaulwoods.adhoc.core.execute.Connection
import spock.lang.Specification


class ConnectionSpec extends Specification {

    def "create a connection"() {

        when:
        Connection connection = Adhoc
                .buildConnection()
                .vendor("mysql")
                .host("localhost")
                .port(3306)
                .username("user")
                .password("pass")
                .database("db")
                .end()


        then:
        connection.vendor == "mysql"
        connection.host == "localhost"
        connection.port == 3306
        connection.username == "user"
        connection.password == "pass"
        connection.database == "db"
    }
}