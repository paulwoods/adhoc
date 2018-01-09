package org.mrpaulwoods.adhoc.core

import org.mrpaulwoods.adhoc.core.schema.Column
import org.mrpaulwoods.adhoc.core.schema.Link
import org.mrpaulwoods.adhoc.core.schema.Schema
import org.mrpaulwoods.adhoc.core.schema.Table
import org.mrpaulwoods.adhoc.core.select.On
import spock.lang.Specification

class ValidateSchemaSpec extends Specification {

    def "no tables"() {

        when:
        Adhoc.buildSchema().end()

        then:
        thrown Schema.NoTablesException
    }

    def "table missing name"() {

        when:
        Adhoc.buildSchema()
                .buildTable()
                .name(null)
                .end()
                .end()

        then:
        thrown Table.InvalidTableNameException
    }

    def "table has no columns"() {

        when:
        Adhoc.buildSchema()
                .buildTable()
                .name("contacts")
                .end()
                .end()

        then:
        thrown Table.NoColumnsException
    }

    def "column name is invalid"() {

        when:
        Adhoc.buildSchema()
                .buildTable()
                .name("contacts")
                .buildColumn().name(null).end()
                .end()
                .end()

        then:
        thrown Column.InvalidColumnNameException
    }

    def "column type is not set"() {

        when:
        Adhoc.buildSchema()
                .buildTable()
                .name("contacts")
                .buildColumn().name("id").end()
                .end()
                .end()

        then:
        thrown Column.ColumnTypeNotSetException
    }

    def "duplicate tables"() {

        when:
        Adhoc.buildSchema()
                .buildTable()
                .name("contacts")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()
                .buildTable()
                .name("contacts")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()
                .end()

        then:
        def e = thrown(Schema.DuplicateTableException)
        e.message == "Table exists more than once: contacts"
    }

    def "duplicate columns"() {

        when:
        Adhoc.buildSchema()
                .buildTable()
                .name("contacts")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()
                .end()

        then:
        def e = thrown(Table.DuplicateColumnException)
        e.message == "Column exists more than once: id"
    }

    def "link table1 is not set"() {

        when:
        Adhoc.buildSchema()
                .buildTable()
                .name("contact")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildTable()
                .name("address")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildLink()
                .end()

                .end()

        then:
        thrown Link.TableNotSetException
    }

    def "link table2 is not set"() {

        when:
        Adhoc.buildSchema()
                .buildTable()
                .name("contact")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildTable()
                .name("address")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildLink()
                .table1("contact")
                .end()

                .end()

        then:
        thrown Link.TableNotSetException
    }

    def "link joinType is not set"() {

        when:
        Adhoc.buildSchema()
                .buildTable()
                .name("contact")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildTable()
                .name("address")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildLink()
                .table1("contact").table2("address")
                .end()

                .end()

        then:
        thrown Link.JoinTypeNotSetException
    }

    def "link cost is not set"() {

        when:
        Adhoc.buildSchema()
                .buildTable()
                .name("contact")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildTable()
                .name("address")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildLink()
                .table1("contact").joinType("left").table2("address")
                .end()

                .end()

        then:
        thrown Link.CostNotSetException
    }

    def "Link table1 duplicates table2"() {

        when:
        Adhoc.buildSchema()
                .buildTable()
                .name("contact")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildTable()
                .name("address")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildLink()
                .table1("contact").joinType("left").table2("contact").cost(500)
                .end()

                .end()


        then:
        thrown Link.DuplicateTableException
    }

    def "Link has no ons"() {

        when:
        Adhoc.buildSchema()
                .buildTable()
                .name("contact")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildTable()
                .name("address")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildLink()
                .table1("contact").joinType("left").table2("address").cost(500)
                .end()

                .end()

        then:
        thrown Link.NoOnsException
    }

    def "On table1 not set"() {

        when:
        Adhoc.buildSchema()
                .buildTable()
                .name("contact")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildTable()
                .name("address")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildLink()
                .table1("contact").joinType("left").table2("address").cost(500)
                .buildOns()
                .end()

                .end()
                .end()

        then:
        thrown On.TableNotSetException
    }

    def "On column1 not set"() {

        when:
        Adhoc.buildSchema()
                .buildTable()
                .name("contact")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildTable()
                .name("address")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildLink()
                .table1("contact").joinType("left").table2("address").cost(500)
                .buildOns().table1("id")
                .end()

                .end()
                .end()

        then:
        thrown On.ColumnNotSetException
    }

    def "On comparison not set"() {

        when:
        Adhoc.buildSchema()
                .buildTable()
                .name("contact")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildTable()
                .name("address")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildLink()
                .table1("contact").joinType("left").table2("address").cost(500)
                .buildOns().table1("contact").column1("id")
                .end()

                .end()
                .end()

        then:
        thrown On.ComparisonNotSetException
    }

    def "On table2 not set"() {

        when:
        Adhoc.buildSchema()
                .buildTable()
                .name("contact")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildTable()
                .name("address")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildLink()
                .table1("contact").joinType("left").table2("address").cost(500)
                .buildOns().table1("contact").column1("id").comparison("=")
                .end()

                .end()
                .end()

        then:
        thrown On.TableNotSetException
    }

    def "On column2 not set"() {

        when:
        Adhoc.buildSchema()
                .buildTable()
                .name("contact")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildTable()
                .name("address")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildLink()
                .table1("contact").joinType("left").table2("address").cost(500)
                .buildOns().table1("contact").column1("id").comparison("=").table2("address")
                .end()

                .end()
                .end()

        then:
        thrown On.ColumnNotSetException
    }

    def "On tables can't be the same"() {

        when:
        Adhoc.buildSchema()
                .buildTable()
                .name("contact")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildTable()
                .name("address")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildLink()
                .table1("contact").joinType("left").table2("address").cost(500)
                .buildOns().table1("contact").column1("id").comparison("=").table2("contact").column2("id")
                .end()

                .end()
                .end()

        then:
        thrown On.TableDuplicateException
    }

    def "Links can't be duplicated"() {

        when:
        Adhoc.buildSchema()
                .buildTable()
                .name("contact")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildTable()
                .name("address")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildLink()
                .table1("contact").joinType("left").table2("address").cost(500)
                .buildOns().table1("contact").column1("id").comparison("=").table2("address").column2("id").end()
                .end()

                .buildLink()
                .table1("contact").joinType("left").table2("address").cost(500)
                .buildOns().table1("contact").column1("id").comparison("=").table2("address").column2("id").end()
                .end()
                .end()

        then:
        thrown Schema.DuplicateLinkException
    }

    def "Ons can't be duplicated"() {

        when:
        Adhoc.buildSchema()
                .buildTable()
                .name("contact")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildTable()
                .name("address")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .end()

                .buildLink()
                .table1("contact").joinType("left").table2("address").cost(500)
                .buildOns().table1("contact").column1("id").comparison("=").table2("address").column2("id").end()
                .buildOns().table1("contact").column1("id").comparison("=").table2("address").column2("id").end()
                .end()

                .end()

        then:
        thrown Link.DuplicateOnException
    }

}
