package org.mrpaulwoods.adhoc.core

import org.mrpaulwoods.adhoc.core.exception.NoSelectsException
import org.mrpaulwoods.adhoc.core.execute.UserValues
import org.mrpaulwoods.adhoc.core.schema.ColumnType
import org.mrpaulwoods.adhoc.core.schema.Comparison
import org.mrpaulwoods.adhoc.core.schema.Schema
import org.mrpaulwoods.adhoc.core.schema.Scope
import org.mrpaulwoods.adhoc.core.select.*
import org.mrpaulwoods.adhoc.core.vendor.MysqlVendor
import spock.lang.Specification

class AdhocSpec extends Specification {

    Schema schema1 = Adhoc.buildSchema()
            .buildTable()
            .name("contact")
            .buildColumn().name("id").columnType("int").nullable(false).end()
            .buildColumn().name("number").columnType("varchar").size(15).nullable(false).end()
            .end()
            .end()

    def "invalid aggregate throws exception"() {

        when:
        Adhoc.buildSelect()
                .buildField().table("contact").column("number").aggregate("invalid").end()
                .end()

        then:
        thrown Aggregate.NotFoundException
    }

    def "invalid column throws exception"() {

        def table = schema1.findTable("contact")

        when:
        schema1.findColumn(table, "invalid name")

        then:
        thrown Schema.ColumnNotFoundException
    }

    def "invalid columnType throws exception"() {

        when:
        ColumnType.findByName("invalid name")

        then:
        thrown ColumnType.NotFoundException
    }

    def "invalid comparison throws exception"() {

        when:
        Comparison.findByName("invalid name")

        then:
        thrown Comparison.NotFoundException
    }

    def "invalid direction throws exception"() {

        when:
        Direction.findByName("invalid name")

        then:
        thrown Direction.NotFoundException
    }

    def "invalid field throws exception"() {

        Select select = Adhoc.buildSelect()
                .buildField().table("contact").column("id").end()
                .end()

        when:
        select.findField("invalid", "invalid")

        then:
        thrown Select.FieldNotFoundException
    }

    def "invalid join type throws exception"() {

        when:
        JoinType.findByName("invalid")

        then:
        thrown JoinType.NotFoundException
    }

    def "no selects throws exception"() {

        MysqlVendor vendor = new MysqlVendor()

        when:
        vendor.createSql(new Schema(), new Select(), new UserValues())

        then:
        thrown(NoSelectsException)
    }

    def "invalid table throws exception"() {

        Schema schema = new Schema()

        when:
        schema.findTable("invalid")

        then:
        thrown Schema.TableNotFoundException
    }

    def "invalid scope throws exception"() {

        when:
        Scope.findByName("invalid")

        then:
        thrown Scope.NotFoundException
    }

    def "invalid conjunction throws exception"() {

        when:
        Conjunction.findByName("invalid")

        then:
        thrown Conjunction.NotFoundException
    }

}