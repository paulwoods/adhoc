package org.mrpaulwoods.adhoc.core

import groovy.sql.Sql
import org.mrpaulwoods.adhoc.core.execute.Connection
import org.mrpaulwoods.adhoc.core.vendor.MysqlVendor
import spock.lang.Specification

class MysqlVendorSpec extends Specification {

    MysqlVendor vendor = new MysqlVendor()

    Sql database = Mock()

    def setup() {
        GroovySpy(Sql, global: true)
        0 * _
    }

    def "parameter is added to url"() {

        Connection connection = Adhoc.buildConnection()
                .vendor("mysql")
                .host("localhost")
                .port(3306)
                .database("contacts")
                .username("user")
                .password("pass")
                .parameter("useSSL", "false")
                .end()

        when:
        vendor.createDatabase(connection)

        then:
        1 * Sql.newInstance(_, _, _, _) >> { url, user, password, driver ->
            assert url == "jdbc:mysql://localhost:3306/contacts?useSSL=false"
        }
    }

    def "parameters are added to url"() {

        Connection connection = Adhoc.buildConnection()
                .vendor("mysql")
                .host("localhost")
                .port(3306)
                .database("contacts")
                .username("user")
                .password("pass")
                .parameter("a", "1")
                .parameter("b", "2")
                .parameter("c", "3")
                .end()

        when:
        vendor.createDatabase(connection)

        then:
        1 * Sql.newInstance(_, _, _, _) >> { url, user, password, driver ->
            assert url == "jdbc:mysql://localhost:3306/contacts?a=1&b=2&c=3"
        }
    }

}

/*

   Sql createDatabase(Connection connection) {
        def url = "jdbc:mysql://$connection.host:$connection.port/$connection.database"
        Sql.newInstance(url, connection.username, connection.password, "com.mysql.jdbc.Driver")
    }

 */