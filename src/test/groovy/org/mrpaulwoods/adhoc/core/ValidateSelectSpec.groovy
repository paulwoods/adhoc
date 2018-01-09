package org.mrpaulwoods.adhoc.core

import org.mrpaulwoods.adhoc.core.select.Field
import org.mrpaulwoods.adhoc.core.select.Order
import org.mrpaulwoods.adhoc.core.select.Select
import org.mrpaulwoods.adhoc.core.select.Where
import spock.lang.Specification

class ValidateSelectSpec extends Specification {

    def "no fields in select throws"() {

        when:
        Adhoc.buildSelect().end()

        then:
        thrown Select.NoFieldsException
    }

    def "table not set in field throws"() {

        when:
        Adhoc.buildSelect()
                .buildField().end()
                .end()

        then:
        thrown Field.TableNotSetException
    }

    def "column not set in field throws"() {

        when:
        Adhoc.buildSelect()
                .buildField().table("contact").end()
                .end()

        then:
        thrown Field.ColumnNotSetException
    }

    def "table not set in where throws"() {

        when:
        Adhoc.buildSelect()
                .buildField().table("contact").column("id").end()
                .buildWhere().end()
                .end()

        then:
        thrown Where.TableNotSetException
    }

    def "column not set in where throws"() {

        when:
        Adhoc.buildSelect()
                .buildField().table("contact").column("id").end()
                .buildWhere().table("contact").end()
                .end()

        then:
        thrown Where.ColumnNotSetException
    }

    def "comparison not set in where throws"() {

        when:
        Adhoc.buildSelect()
                .buildField().table("contact").column("id").end()
                .buildWhere().table("contact").column("id").end()
                .end()

        then:
        thrown Where.ComparisonNotSetException
    }

    def "conjunction not set in where throws"() {

        when:
        Adhoc.buildSelect()
                .buildField().table("contact").column("id").end()
                .buildWhere().table("contact").column("id").comparison("=").end()
                .end()

        then:
        thrown Where.ConjunctionNotSetException
    }

    def "table not set in order throws"() {

        when:
        Adhoc.buildSelect()
                .buildField().table("contact").column("id").end()
                .buildWhere().conjunction("and").table("contact").column("id").comparison("=").end()
                .buildOrder().end()
                .end()

        then:
        thrown Order.TableNotSetException
    }

    def "column not set in order throws"() {

        when:
        Adhoc.buildSelect()
                .buildField().table("contact").column("id").end()
                .buildWhere().conjunction("and").table("contact").column("id").comparison("=").end()
                .buildOrder().table("contact").end()
                .end()

        then:
        thrown Order.ColumnNotSetException
    }

    def "direction not set in order throws"() {

        when:
        Adhoc.buildSelect()
                .buildField().table("contact").column("id").end()
                .buildWhere().conjunction("and").table("contact").column("id").comparison("=").end()
                .buildOrder().table("contact").column("id").end()
                .end()

        then:
        thrown Order.DirectionNotSetException
    }

}
