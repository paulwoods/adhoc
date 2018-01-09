package org.mrpaulwoods.adhoc.core

import org.mrpaulwoods.adhoc.core.schema.Schema
import spock.lang.Specification

class SchemaPersistSpec extends Specification {

    Schema schema1 = Adhoc.buildSchema()
            .buildTable()
            .name("contact")
            .buildColumn().name("id").columnType("int").nullable(false).end()
            .buildColumn().name("number").columnType("varchar").size(15).nullable(false).end()
            .end()

            .buildTable()
            .name("address")
            .buildColumn().name("id").columnType("int").nullable(false).end()
            .buildColumn().name("filename").columnType("varchar").size(15).nullable(false).end()
            .end()

            .buildLink()
            .table1("contact").joinType("left").table2("address").cost(500)
            .buildOns().table1("contact").column1("id").comparison("=").table2("address").column2("contact_id").end()
            .end()

            .end()

    def "schema can be saved and loaded"() {
        def baos = new ByteArrayOutputStream()
        def oos = new ObjectOutputStream(baos)

        when:
        oos.writeObject(schema1)

        then:
        baos.size() > 0

        when:
        def bais = new ByteArrayInputStream(baos.toByteArray())
        def ois = new ObjectInputStream(bais)

        Schema schema2 = (Schema) ois.readObject()

        then:
        schema2.tables.size() == 2
        schema2.tables[0].name == "contact"
        schema2.tables[0].columns.size() == 2
        schema2.tables[0].columns[0].name == "id"
        schema2.tables[0].columns[0].columnType.name == "int"
        !schema2.tables[0].columns[0].nullable

        schema2.tables[0].columns[1].name == "number"
        schema2.tables[0].columns[1].columnType.name == "varchar"
        schema2.tables[0].columns[1].size == 15
        !schema2.tables[0].columns[1].nullable

        schema2.tables[1].name == "address"
        schema2.tables[1].columns.size() == 2
        schema2.tables[1].columns[0].name == "id"
        schema2.tables[1].columns[0].columnType.name == "int"
        !schema2.tables[1].columns[0].nullable

        schema2.tables[1].columns[1].name == "filename"
        schema2.tables[1].columns[1].columnType.name == "varchar"
        schema2.tables[1].columns[1].size == 15
        !schema2.tables[1].columns[1].nullable

        schema2.links.size() == 1
        schema2.links[0].table1.name == "contact"
        schema2.links[0].joinType.name == "left"
        schema2.links[0].table2.name == "address"
        schema2.links[0].ons.size() == 1
        schema2.links[0].ons[0].table1 == "contact"
        schema2.links[0].ons[0].column1 == "id"
        schema2.links[0].ons[0].comparison.name == "="
        schema2.links[0].ons[0].table2 == "address"
        schema2.links[0].ons[0].column2 == "contact_id"
    }

}
