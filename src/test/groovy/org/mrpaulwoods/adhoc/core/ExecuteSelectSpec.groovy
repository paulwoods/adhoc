package org.mrpaulwoods.adhoc.core

import groovy.sql.Sql
import org.mrpaulwoods.adhoc.core.execute.Connection
import org.mrpaulwoods.adhoc.core.execute.UserValues
import org.mrpaulwoods.adhoc.core.report.Report
import org.mrpaulwoods.adhoc.core.schema.Schema
import org.mrpaulwoods.adhoc.core.select.Select
import spock.lang.Specification

class ExecuteSelectSpec extends Specification {

    Schema schema1 = Adhoc.buildSchema()
            .buildTable()
            .name("contact")
            .buildColumn().name("id").columnType("int").nullable(false).end()
            .buildColumn().name("number").columnType("varchar").size(15).nullable(false).end()
            .end()
            .end()

    Connection connection1 = Adhoc.buildConnection()
            .vendor("mysql")
            .host("localhost")
            .port(3306)
            .database("contacts")
            .username("user")
            .password("pass")
            .end()

    Select select1 = Adhoc
            .buildSelect().buildField().table("contact").column("number").end()
            .buildWhere().conjunction("and").table("contact").column("number").comparison(">").value("20150000000").end()
            .buildOrder().table("contact").column("number").direction("asc").end()
            .end()

    Select select2 = Adhoc
            .buildSelect().buildField().table("contact").column("number").end()
            .buildWhere().conjunction("and").table("contact").column("number").comparison(">").end()
            .buildOrder().table("contact").column("number").direction("asc").end()
            .end()

    Select select3 = Adhoc
            .buildSelect().buildField().table("contact").column("number").end()
            .buildWhere().conjunction("and").conjunction("and").table("contact").column("number").comparison(">").end()
            .buildWhere().conjunction("and").conjunction("and").table("contact").column("id").comparison("<>").end()
            .buildOrder().table("contact").column("number").direction("asc").end()
            .end()

    Sql database = Mock()

    def setup() {
        GroovySpy(Sql, global: true)
        0 * _
    }

    def "execute query"() {

        when:
        Report report = Adhoc.buildExecute()
                .vendor("mysql")
                .connection(connection1)
                .schema(schema1)
                .select(select1)
                .end()

        then:
        1 * Sql.newInstance(_, _, _, _) >> database

        1 * database.rows(_, _) >> { query, values ->
            assert values == []
            [[contact_number: "111"], [contact_number: "222"]]
        }

        1 * database.close()

        and:
        report.headers.size() == 1
        report.headers[0].title == "contact_number"

        report.rows.size() == 2
        report.rows[0].fields.size() == 1
        report.rows[0].fields[0].value == "111"
        report.rows[1].fields.size() == 1
        report.rows[1].fields[0].value == "222"
    }

    def "retrieve the needed user values - 1 value"() {
        when:
        def values = select2.determineUserValues()

        then:
        values.size() == 1
        values[0] == "0.contact.number"
    }

    def "execute query with 1 user defined value"() {

        UserValues userValues1 = new UserValues()
                .add("0.contact.number", "20150101000")


        when:
        Adhoc.buildExecute()
                .vendor("mysql")
                .connection(connection1)
                .schema(schema1)
                .select(select2)
                .userValues(userValues1)
                .end()

        then:
        1 * Sql.newInstance(_, _, _, _) >> database

        1 * database.rows(_, _) >> { sql, values ->
            assert values == ["20150101000"]
            [[contact_number: "333"], [contact_number: "444"]]
        }

        1 * database.close()

        and:
        userValues1.size() == 1
    }

    def "retrieve the needed user values - 2 values"() {

        when:
        def values = select3.determineUserValues()

        then:
        values.size() == 2
        values[0] == "0.contact.number"
        values[1] == "1.contact.id"
    }


    def "execute query with 2 user defined values"() {

        UserValues userValues1 = new UserValues()
                .add("0.contact.number", "20150101000")
                .add("1.contact.id", 123)

        when:
        Adhoc.buildExecute()
                .vendor("mysql")
                .connection(connection1)
                .schema(schema1)
                .select(select3)
                .userValues(userValues1)
                .end()

        then:
        1 * Sql.newInstance(_, _, _, _) >> database

        1 * database.rows(_, _) >> { sql, values ->
            assert values == ["20150101000", 123]
            [[contact_number: "333"], [contact_number: "444"]]
        }

        1 * database.close()

        and:
        userValues1.size() == 2
    }

    Select select4 = Adhoc
            .buildSelect().buildField().table("contact").column("number").end()
            .buildWhere().conjunction("and").table("contact").column("number").comparison("between").end()
            .end()

    def "retrieve the needed user values - between values"() {

        when:
        def values = select4.determineUserValues()

        then:
        values.size() == 2
        values[0] == "0.contact.number.lower"
        values[1] == "0.contact.number.upper"
    }

    def "execute query with 2 user defined values and between where"() {

        UserValues userValues1 = new UserValues()
                .add("0.contact.number.lower", "2010")
                .add("0.contact.number.upper", "2015")

        when:
        Adhoc.buildExecute()
                .vendor("mysql")
                .connection(connection1)
                .schema(schema1)
                .select(select4)
                .userValues(userValues1)
                .end()

        then:
        1 * Sql.newInstance(_, _, _, _) >> database

        1 * database.rows(_, _) >> { sql, values ->
            assert values == ["2010", "2015"]
            [[contact_number: "333"], [contact_number: "444"]]
        }

        1 * database.close()
    }

    Select select5 = Adhoc
            .buildSelect().buildField().table("contact").column("number").end()
            .buildWhere().conjunction("and").table("contact").column("number").comparison("in list").end()
            .end()

    def "retrieve the needed user values - in list - 1 value"() {

        when:
        def values = select5.determineUserValues()

        then:
        values.size() == 1
        values[0] == "0.contact.number"
    }

    def "execute query with 1 user defined values and in list where - 1 list item"() {

        UserValues userValues1 = new UserValues()
                .add("0.contact.number", "2010")

        when:
        Adhoc.buildExecute()
                .vendor("mysql")
                .connection(connection1)
                .schema(schema1)
                .select(select5)
                .userValues(userValues1)
                .end()

        then:
        1 * Sql.newInstance(_, _, _, _) >> database

        1 * database.rows(_, _) >> { sql, values ->
            assert values == ["2010"]
            [[contact_number: "333"], [contact_number: "444"]]
        }

        1 * database.close()
    }

    def "execute query with 1 user defined values and in list where - 2 list items"() {

        UserValues userValues1 = new UserValues()
                .add("0.contact.number", ["2010", "2011"])

        when:
        Adhoc.buildExecute()
                .vendor("mysql")
                .connection(connection1)
                .schema(schema1)
                .select(select5)
                .userValues(userValues1)
                .end()

        then:
        1 * Sql.newInstance(_, _, _, _) >> database

        1 * database.rows(_, _) >> { sql, values ->
            assert sql == '''select
                            |\tcontact.number as "contact_number"
                            |
                            |from contact
                            |
                            |where contact.number in (?, ?)'''.stripMargin()
            assert values == ["2010", "2011"]
            []
        }

        1 * database.close()
    }


    def "execute query multiple wheres"() {

        Select select6 = Adhoc
                .buildSelect().buildField().table("contact").column("number").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("=").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("null").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("in list").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison("<>").end()
                .buildWhere().conjunction("and").table("contact").column("number").comparison(">").end()
                .end()


        UserValues userValues1 = new UserValues()
                .add("0.contact.number", "2009")
                .add("2.contact.number", ["2010", "2011", "2012"])
                .add("3.contact.number", "2013")
                .add("4.contact.number", "2014")

        when:
        Adhoc.buildExecute()
                .vendor("mysql")
                .connection(connection1)
                .schema(schema1)
                .select(select6)
                .userValues(userValues1)
                .end()

        then:
        1 * Sql.newInstance(_, _, _, _) >> database

        1 * database.rows(_, _) >> { sql, values ->
            assert values == ["2009", "2010", "2011", "2012", "2013", "2014"]
            []
        }

        1 * database.close()
    }

    def "title in the schema"() {
        schema1.tables[0].columns[1].title = "The Contact Number"

        when:
        Report report = Adhoc.buildExecute()
                .vendor("mysql")
                .connection(connection1)
                .schema(schema1)
                .select(select1)
                .end()

        then:
        1 * Sql.newInstance(_, _, _, _) >> database

        1 * database.rows(_, _) >> { query, values ->
            assert values == []
            [[contact_number: "111"], [contact_number: "222"]]
        }

        1 * database.close()

        and:
        report.headers[0].title == "The Contact Number"
    }

    def "title in the schema and select"() {
        schema1.tables[0].columns[1].title = "The Contact Number"
        select1.fields[0].title = "The Selects Contact Number"

        when:
        Report report = Adhoc.buildExecute()
                .vendor("mysql")
                .connection(connection1)
                .schema(schema1)
                .select(select1)
                .end()

        then:
        1 * Sql.newInstance(_, _, _, _) >> database

        1 * database.rows(_, _) >> { query, values ->
            assert values == []
            [[contact_number: "111"], [contact_number: "222"]]
        }

        1 * database.close()

        and:
        report.headers[0].title == "The Selects Contact Number"
    }
}

