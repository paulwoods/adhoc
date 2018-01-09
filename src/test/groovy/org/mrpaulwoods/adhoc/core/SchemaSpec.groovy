package org.mrpaulwoods.adhoc.core

import org.mrpaulwoods.adhoc.core.schema.Schema
import spock.lang.Specification

class SchemaSpec extends Specification {

    def "create a schema"() {
        Schema schema = Adhoc.buildSchema()
                .buildTable()
                .name("contact")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .buildColumn().name("number").columnType("varchar").size(15).nullable(false).end()
                .end()
                .end()
        expect:
        schema.tables[0].name == "contact"
        schema.tables[0].columns[0].name == "id"
        schema.tables[0].columns[0].columnType.name == "int"
        !schema.tables[0].columns[0].nullable
        schema.tables[0].columns[1].name == "number"
        schema.tables[0].columns[1].columnType.name == "varchar"
        schema.tables[0].columns[1].size == 15
        !schema.tables[0].columns[1].nullable
    }

    def "a column can have a title"() {
        Schema schema = Adhoc.buildSchema()
                .buildTable()
                .name("contact")
                .buildColumn().name("id").columnType("int").nullable(false).end()
                .buildColumn().name("number").columnType("varchar").size(15).nullable(false).title("Contact Number").end()
                .end()
                .end()

        expect:
        schema.tables[0].columns[1].title == "Contact Number"
    }

}